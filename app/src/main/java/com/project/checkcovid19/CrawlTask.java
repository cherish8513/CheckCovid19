package com.project.checkcovid19;

import java.util.TimerTask;

public class CrawlTask extends TimerTask {
    public void run() {
        ThreadWeb crawler = new ThreadWeb();
        Thread thread = new Thread(crawler);
        thread.start();
    }
}