package com.project.checkcovid19.service;

import com.project.checkcovid19.crawl.CrawlRunnable;
import com.project.checkcovid19.dao.CovidDao;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlService implements Runnable{
    private CovidDao dao;
    public CrawlService(CovidDao dao){
        this.dao = dao;
    }
    public synchronized void run() {
        ExecutorService execService = Executors.newSingleThreadExecutor();
        execService.execute(new CrawlRunnable(dao));
    }
}