package com.project.checkcovid19.dto;

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

    public synchronized boolean select(String area){
        return Db.select(area);
    }

    public synchronized ArrayList<Integer> compare(){
        return Db.compare();
    }
}
