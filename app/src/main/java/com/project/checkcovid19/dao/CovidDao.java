package com.project.checkcovid19.dao;

import android.content.Context;

import com.project.checkcovid19.domain.Covid;

import java.util.ArrayList;

public class CovidDao {
    Covid Db;
    public CovidDao(Context context){
        Db = Covid.getInstance(context);
    }

    public synchronized boolean  update(String area, int num){
        return Db.update(area, num);
    }

    public synchronized int select(String area){
        if(area.equals("서울특별시"))
            return Db.select("seoul");
        if(area.equals( "부산광역시"))
            return Db.select("busan");
        if(area.equals( "인천광역시"))
            return Db.select("incheon");
        if(area.equals( "대구광역시"))
            return Db.select("daegu");
        if(area.equals( "광주광역시"))
            return Db.select("gwangju");
        if(area.equals( "대전광역시"))
            return Db.select("daejeon");
        if(area.equals( "울산광역시"))
            return Db.select("ulsan");
        if(area.equals( "경기도"))
            return Db.select("kyeonggi");
        if(area.equals( "강원도"))
            return Db.select("kangwon");
        if(area.equals( "충청북도"))
            return Db.select("chungbuk");
        if(area.equals( "충청남도"))
            return Db.select("chungnam");
        if(area.equals( "전라북도"))
            return Db.select("jeonbuk");
        if(area.equals( "전라남도"))
            return Db.select("jeonnam");
        if(area.equals( "경상북도"))
            return Db.select("kyeongbuk");
        if(area.equals( "경상남도"))
            return Db.select("kyeongnam");
        if(area.equals( "제주도"))
            return Db.select("jeju");
        return -1;
    }

    public synchronized ArrayList<Integer> compare(){
        return Db.compare();
    }
}
