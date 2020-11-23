package com.project.checkcovid19.crawl;

import com.project.checkcovid19.dao.CovidDao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CrawlRunnable implements Runnable{
    private Document doc;
    private String covid_data;
    private Elements elements;
    private String[] new_patient;
    private int search_num;
    private CovidDao dao;

    public CrawlRunnable(CovidDao dao){
        search_num = 100;
        this.dao = dao;
    }
    @Override
    public void run() {
        try {
            doc = Jsoup.connect("http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13&ncvContSeq=&contSeq=&board_id=&gubun=").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        elements = doc.select("div.data_table table tbody tr");

        if (elements != null) {
            covid_data = elements.text();
        }
        new_patient = covid_data.split(" ");
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread thread1 = new Thread(new SearchRunnable(new_patient, search_num, dao));
        Thread thread2 = new Thread(new SearchRunnable(new_patient,search_num*2, dao));
        thread1.start();
        thread2.start();
    }
}
