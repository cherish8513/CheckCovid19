package com.project.checkcovid19.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.checkcovid19.R;
import com.project.checkcovid19.constants.Constants;
import com.project.checkcovid19.dao.CovidDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.project.checkcovid19.constants.Constants.GPS_ENABLE_REQUEST_CODE;
import static com.project.checkcovid19.constants.Constants.PERMISSIONS_REQUEST_CODE;


public class MainActivity extends AppCompatActivity  {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    private Address addressOutput;
    private String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private TextView covid_information_tv;
    private TextView covid_ranking_tv;
    private Button refresh_btn;
    private GpsService gpsService;
    private BroadcastReceiver addressReceiver;
    private boolean addressRequested;
    private int numOfPatient;
    private Rankings rankings;
    private int count;
    private int rank;
    private boolean permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        covid_information_tv = (TextView)findViewById(R.id.covid_information_tv);
        covid_ranking_tv = (TextView)findViewById(R.id.covid_ranking_tv);
        refresh_btn = (Button)findViewById(R.id.refresh_btn);

        final CovidDao covidDao = new CovidDao(this);
        CrawlService crawlTask = new CrawlService(covidDao);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        rankings = new Rankings(this.getFilesDir()+"/"+Constants.file_name);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
        executor.scheduleAtFixedRate(crawlTask, calcTaskTime(2),24*60*60*1000, TimeUnit.SECONDS);
        Thread thread = new Thread(crawlTask);
        thread.start();



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){}
        else{
            final ServiceConnection conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    GpsService.LocalBinder mb = (GpsService.LocalBinder) service;
                    gpsService = mb.getService();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.d("서비스 연결", " 해제되었습니다");
                }
            };

            Intent intent = new Intent(getApplicationContext(), GpsService.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
        }

        addressReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                addressOutput = intent.getExtras().getParcelable(Constants.data_name);
                if(addressOutput.getAdminArea() == null)
                    Toast.makeText(MainActivity.this,"미확인 지역입니다.",Toast.LENGTH_LONG).show();
                else {
                    numOfPatient = covidDao.select(addressOutput.getAdminArea());
                }
                addressRequested = false;
                rankings.saveLog(addressOutput.getAdminArea(), numOfPatient);
                count = rankings.getCurrent_num_of_data();
                rank = rankings.getRank();

                displayAddressOutput();
                displayRankOutput();
                updateUIWidgets();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver( addressReceiver, new IntentFilter(Constants.service_name));

        addressRequested = true;
        updateUIWidgets();
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    addressRequested = true;
                    updateUIWidgets();
                    gpsService.getGps();
                }catch(SecurityException ex){
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        rankings.closeFile();
    }

    public void displayAddressOutput(){
        //covid_information_tv.setText(addressOutput.getAddressLine(0).substring(5) + " 의 신규 확진자 수는 " + numOfPatient + " 명 입니다");
        covid_information_tv.setText(addressOutput.getAdminArea() + " 신규 확진자 :  " + numOfPatient + " 명");
        Intent widgetIntent = new Intent(this, CheckCovid19Widget.class);
        widgetIntent.putExtra("text1",addressOutput.getAdminArea());
        widgetIntent.putExtra("text2"," 신규 확진자 " + numOfPatient + "명");
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        this.sendBroadcast(widgetIntent);
    }

    public void displayRankOutput(){
        covid_ranking_tv.setText(addressOutput.getAdminArea() + "의 신규 확진자 " + numOfPatient + " 명은 현재 " +
                count + "개의 지역 혹은 날짜에서 " + rank + "위 입니다");
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
    }


    private void updateUIWidgets() {
        if (addressRequested) {
            refresh_btn.setEnabled(false);
        } else {
            refresh_btn.setEnabled(true);
        }
    }


    public long calcTaskTime(int startTime) {

        if(startTime > 23 || startTime < 0){
            return 0;
        }
        if(startTime < 9)
            startTime = startTime+24-9;
        else
            startTime = startTime-9;

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, startTime);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        long nowDate = new Date().getTime();

        if (nowDate > calendar.getTime().getTime()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        long waiting = (calendar.getTime().getTime() - nowDate)/1000;
        Date setDate = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
        String setTime = formatter.format(setDate.getTime() + (long)(1000*60*60*9));
        Log.i("date","Schedule Start Time : " + setTime);
        Log.i("wating","Waiting : " + waiting/3600+" hour " + waiting%3600/60 + " minute " + waiting%3600%60 + " sec");

        return (int)waiting;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}

