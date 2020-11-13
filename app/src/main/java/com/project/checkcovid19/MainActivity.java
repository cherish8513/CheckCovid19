package com.project.checkcovid19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ThreadWeb thread_a = new ThreadWeb();
        String area = "대구";
        Thread thread1 = new Thread(thread_a, area);

        thread1.start();

    }
}

class ThreadWeb implements Runnable{
    private Document doc;
    private String new_patient;
    private Elements elements;
    @Override
    public void run() {
        synchronized (this) {
            switch (Thread.currentThread().getName()){
                case "서울":
                    try {
                        doc = Jsoup.connect("https://www.seoul.go.kr/coronaV/coronaStatus.do").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    elements = doc.select("div.num-wrap-new p");

                    if(elements != null){
                        new_patient = elements.text();
                    }
                    new_patient = new_patient.substring(0, new_patient.indexOf(" "));
                    System.out.println("신규 확진자 수 : " + Integer.parseInt(new_patient));
                    break;
                case "대구":
                    try {
                        doc = Jsoup.connect("http://covid19.daegu.go.kr").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    elements = doc.select("ul.confirm-box-new");

                    if(elements != null){
                        new_patient = elements.text();
                    }
                    //new_patient = new_patient.substring(0, new_patient.indexOf(" "));
                    //System.out.println("신규 확진자 수 : " + Integer.parseInt(new_patient));
                    System.out.println("신규 확진자 수 : " + elements);
                    break;
            }
        }
    }
}