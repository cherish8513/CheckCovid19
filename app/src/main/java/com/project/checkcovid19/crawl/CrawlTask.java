package com.project.checkcovid19.crawl;

import android.util.Log;

import com.project.checkcovid19.service.ComparePatient;
import com.project.checkcovid19.service.CovidDao;

import java.util.ArrayList;
import java.util.TimerTask;

public class CrawlTask extends TimerTask {
    private CovidDao dao;
    public CrawlTask(CovidDao dao){
        this.dao = dao;
    }
    public synchronized void run() {
        ComparePatient.showData(dao);
        CrawlRunnable crawler = new CrawlRunnable(dao);
        Thread thread = new Thread(crawler);
        thread.start();
    }
}