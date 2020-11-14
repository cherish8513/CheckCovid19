package com.project.checkcovid19.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public  class ComparePatient {
    private static String[] area = new String[]{"서울", "부산", "대구", "인천", "광주",
            "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북",
            "전남", "경북", "경남", "제주", "검역"};

    public static void showData(CovidDao dao){
        ArrayList<Integer> data = dao.compare();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd HH시 mm분");
        String cTime = formatter.format(date.getTime() + (long)(1000*60*60*9));

        for(int i = 0; i < data.size(); i++){
            System.out.println( cTime +" "+ area[i] + "의 신규 확진자 수는 " + data.get(i) + "명 입니다");
        }
    }
}
