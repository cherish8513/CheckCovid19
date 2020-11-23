package com.project.checkcovid19.domain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Covid {
    static final String DB_COVID = "Covid.db";
    static final String TABLE_NAME = "COVID_PATIENT_T";
    static final int DB_VERSION = 1;

    Context myContext = null;

    private static Covid covidDBManager = null;
    private SQLiteDatabase covidDB = null;


    public static Covid getInstance(Context context)
    {
        if(covidDBManager == null)
        {
            covidDBManager = new Covid(context);
        }

        return covidDBManager;
    }

    private Covid(Context context)
    {
        myContext = context;
        try {
            covidDB = context.openOrCreateDatabase(DB_COVID, context.MODE_PRIVATE, null);
        } catch (Exception e) {
            e.printStackTrace();
        }


        covidDB.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "seoul INTEGER," +
                "busan INTEGER," +
                "daegu INTEGER," +
                "incheon INTEGER," +
                "gwangju INTEGER," +
                "daejeon INTEGER," +
                "ulsan INTEGER," +
                "sejong INTEGER," +
                "kyeonggi INTEGER," +
                "kangwon INTEGER," +
                "chungbuk INTEGER," +
                "chungnam INTEGER," +
                "jeonbuk INTEGER," +
                "jeonnam INTEGER," +
                "kyeongbuk INTEGER," +
                "kyeongnam INTEGER," +
                "jeju INTEGER," +
                "quarantine INTEGER);");


        String sqlInsert = "INSERT INTO " + TABLE_NAME + "(seoul, busan, daegu, incheon, gwangju, daejeon, ulsan, sejong," +
                "kyeonggi, kangwon, chungbuk, chungnam, jeonbuk, jeonnam, kyeongbuk, kyeongnam, jeju, quarantine) VALUES (1, 1, 1, 1, 1, 1, 1, 1, " +
                "1, 1, 1, 1, 1, 1, 1, 1, 1, 1)";

        covidDB.execSQL(sqlInsert);
    }

    public boolean update(String area, int num){
        if(covidDB == null){
            return false;
        }
        String sqlUpdate = "UPDATE "+ TABLE_NAME  +" SET "+ area + " = " + num + " WHERE _id = 1" ;
        covidDB.execSQL(sqlUpdate) ;
        return true;
    }

    public int select(String area){
        int num = -1;
        if(covidDB == null){
            return num;
        }
        String sqlSelect = "SELECT " + area + " FROM " + TABLE_NAME + " WHERE _id = 1";
        Cursor cursor = null ;
        cursor = covidDB.rawQuery(sqlSelect, null) ;
        while (cursor.moveToNext()) {
            num = cursor.getInt(0);
        }
        return num;
    }

    public ArrayList<Integer> compare(){
        if(covidDB == null){
            return null;
        }
        ArrayList<Integer> data = new ArrayList<>();
        String sqlSelect = "SELECT * FROM " + TABLE_NAME + " WHERE _id = 1";
        Cursor cursor = null ;
        cursor = covidDB.rawQuery(sqlSelect, null) ;
        while (cursor.moveToNext()) {
            for(int i = 1; i < cursor.getColumnCount(); i++){
                data.add(cursor.getInt(i));
            }
        }
        return data;
    }
}
