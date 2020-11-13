package com.project.checkcovid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CrawlTask crawlTask = new CrawlTask();
        Timer crawlingCalender = new Timer();

        crawlingCalender.scheduleAtFixedRate(crawlTask, calcTaskTime(2),24*60*60*1000);
    }

    public long calcTaskTime(int startTime) {

        if(startTime > 23 || startTime < 0){
            return 0;
        }
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        calendar.set(Calendar.HOUR_OF_DAY, startTime);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        long nowDate = new Date().getTime();

        if (nowDate > calendar.getTime().getTime()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        long waiting = (calendar.getTime().getTime() - nowDate)/1000;
        Log.i("date","Schedule Start Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
        Log.i("wating","Waiting : " + waiting+" sec");

        return (int)waiting;
    }

}

