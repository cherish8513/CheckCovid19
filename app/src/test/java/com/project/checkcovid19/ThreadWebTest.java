package com.project.checkcovid19;

import com.project.checkcovid19.crawl.SearchRunnable;
import com.project.checkcovid19.dto.CovidDao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ThreadWebTest {

    private Document doc;
    private Elements elements;
    private String covid_data;
    private String[] new_patient = null;
    private int search_num;
    private int start_num;
    private String test;

    @Before
    public void setUp() throws Exception {
        start_num = 100;
        search_num = start_num;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void run() {
        CovidDao dao = null;
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread1 = new Thread(new SearchRunnable(new_patient, search_num, dao));
        Thread thread2 = new Thread(new SearchRunnable(new_patient, search_num * 2, dao));
        thread2.start();
        thread1.start();
    }
}