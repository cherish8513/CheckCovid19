package com.project.checkcovid19.crawl;

import com.project.checkcovid19.service.CovidDao;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlTask implements Runnable{
    private CovidDao dao;
    public CrawlTask(CovidDao dao){
        this.dao = dao;
    }
    public synchronized void run() {
        ExecutorService execService = Executors.newSingleThreadExecutor();
        execService.execute(new CrawlRunnable(dao));
        execService.execute(new CompareRunnable(dao));
    }
}