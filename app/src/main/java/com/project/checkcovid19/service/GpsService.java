package com.project.checkcovid19.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.project.checkcovid19.constants.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GpsService extends Service {
    Location lastLocation;
    LocationListener mLocationListener;
    LocationManager lm;
    Address address;

    private final IBinder mBinder = new LocalBinder();

    class LocalBinder extends Binder {
        GpsService getService() {
            return GpsService.this;
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getGps();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getGps();
        return super.onStartCommand(intent, flags, startId);
    }

    public void getGps() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ;
        }

        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lastLocation = location;
                ExecutorService execService = Executors.newSingleThreadExecutor();
                execService.execute(new Runnable() {
                    @Override
                    public void run() {
                        Geocoder geocoder = new Geocoder(GpsService.this, Locale.KOREA);
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(
                                    lastLocation.getLatitude(),
                                    lastLocation.getLongitude(),
                                    // In this sample, we get just a single address.
                                    1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        address = addresses.get(0);
                        Intent intent = new Intent(Constants.service_name);
                        intent.putExtra(Constants.data_name, address);
                        LocalBroadcastManager.getInstance(GpsService.this).sendBroadcast(intent);
                    }
                });
                Log.d("test", "onLocationChanged, location:" + location);
            }
            public void onProviderDisabled(String provider) {
                Log.d("test", "onProviderDisabled, provider:" + provider);
            }

            public void onProviderEnabled(String provider) {
                Log.d("test", "onProviderEnabled, provider:" + provider);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 60 * 60,
                500,
                mLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000*60*60,
                500,
                mLocationListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
