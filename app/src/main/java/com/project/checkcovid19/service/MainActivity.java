package com.project.checkcovid19.service;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.project.checkcovid19.R;
import com.project.checkcovid19.crawl.CrawlRunnable;
import com.project.checkcovid19.crawl.CrawlTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CovidDao covidDao = new CovidDao(this);

        CrawlTask crawlTask = new CrawlTask(covidDao);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(crawlTask, calcTaskTime(1),24*60*60*1000, TimeUnit.SECONDS);
        //Thread thread = new Thread(crawlTask);
        //thread.start();
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd HH시 mm분");
        String setTime = formatter.format(setDate.getTime() + (long)(1000*60*60*9));
        Log.i("date","Schedule Start Time : " + setTime);
        Log.i("wating","Waiting : " + waiting/3600+" hour " + waiting%3600/60 + " minute " + waiting%3600%60 + " sec");

        return (int)waiting;
    }

}

