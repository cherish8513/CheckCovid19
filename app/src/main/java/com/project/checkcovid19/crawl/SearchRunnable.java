package com.project.checkcovid19.crawl;

import com.project.checkcovid19.dto.CovidDao;

import java.util.ArrayList;

public class SearchRunnable implements Runnable {
    private String[] target;
    private int num;
    private CovidDao dao;
    private ArrayList<String> data;

    public SearchRunnable(String[] target, int num, CovidDao dao){
        this.target = target;
        this.num = num;
        this.dao = dao;
        data = new ArrayList<>();
    }

    @Override
    public void run() {
        if(target.length > num){
            for(int i = 0; i<num;i++){
                if(target[i].equals("서울")) {
                    data.add("seoul");
                    data.add(target[++i]);
                }
                else if(target[i].equals("부산")){
                    data.add("busan");
                    data.add(target[++i]);
                }
                else if(target[i].equals("대구")){
                    data.add("daegu");
                    data.add(target[++i]);
                }
                else if(target[i].equals("인천")){
                    data.add("incheon");
                    data.add(target[++i]);
                }
                else if(target[i].equals("광주")){
                    data.add("gwangju");
                    data.add(target[++i]);
                }
                else if(target[i].equals("대전")){
                    data.add("daejeon");
                    data.add(target[++i]);
                }
                else if(target[i].equals("울산")){
                    data.add("ulsan");
                    data.add(target[++i]);
                }
                else if(target[i].equals("세종")){
                    data.add("sejong");
                    data.add(target[++i]);
                }
                else if(target[i].equals("경기")){
                    data.add("kyeonggi");
                    data.add(target[++i]);
                }
                else if(target[i].equals("강원")){
                    data.add("kangwon");
                    data.add(target[++i]);
                }
                else if(target[i].equals("충북")){
                    data.add("chungbuk");
                    data.add(target[++i]);
                }
                else if(target[i].equals("충남")){
                    data.add("chungnam");
                    data.add(target[++i]);
                }
                else if(target[i].equals("전북")){
                    data.add("jeonbuk");
                    data.add(target[++i]);
                }
                else if(target[i].equals("전남")){
                    data.add("jeonnam");
                    data.add(target[++i]);
                }
                else if(target[i].equals("경북")){
                    data.add("kyeongbuk");
                    data.add(target[++i]);
                }
                else if(target[i].equals("경남")){
                    data.add("kyeongbuk");
                    data.add(target[++i]);
                }
                else if(target[i].equals("제주")){
                    data.add("jeju");
                    data.add(target[++i]);
                }
                else if(target[i].equals("검역")){
                    data.add("quarantine");
                    data.add(target[++i]);
                }
            }
        }
        else {
            for (int i = num / 2; i < target.length; i++) {
                if(target[i].equals("서울")) {
                    data.add("seoul");
                    data.add(target[++i]);
                }
                else if(target[i].equals("부산")){
                    data.add("busan");
                    data.add(target[++i]);
                }
                else if(target[i].equals("대구")){
                    data.add("daegu");
                    data.add(target[++i]);
                }
                else if(target[i].equals("인천")){
                    data.add("incheon");
                    data.add(target[++i]);
                }
                else if(target[i].equals("광주")){
                    data.add("gwangju");
                    data.add(target[++i]);
                }
                else if(target[i].equals("대전")){
                    data.add("daejeon");
                    data.add(target[++i]);
                }
                else if(target[i].equals("울산")){
                    data.add("ulsan");
                    data.add(target[++i]);
                }
                else if(target[i].equals("세종")){
                    data.add("sejong");
                    data.add(target[++i]);
                }
                else if(target[i].equals("경기")){
                    data.add("kyeonggi");
                    data.add(target[++i]);
                }
                else if(target[i].equals("강원")){
                    data.add("kangwon");
                    data.add(target[++i]);
                }
                else if(target[i].equals("충북")){
                    data.add("chungbuk");
                    data.add(target[++i]);
                }
                else if(target[i].equals("충남")){
                    data.add("chungnam");
                    data.add(target[++i]);
                }
                else if(target[i].equals("전북")){
                    data.add("jeonbuk");
                    data.add(target[++i]);
                }
                else if(target[i].equals("전남")){
                    data.add("jeonnam");
                    data.add(target[++i]);
                }
                else if(target[i].equals("경북")){
                    data.add("kyeongbuk");
                    data.add(target[++i]);
                }
                else if(target[i].equals("경남")){
                    data.add("kyeongbuk");
                    data.add(target[++i]);
                }
                else if(target[i].equals("제주")){
                    data.add("jeju");
                    data.add(target[++i]);
                }
                else if(target[i].equals("검역")){
                    data.add("quarantine");
                    data.add(target[++i]);
                }
            }
        }
        for(int i = 0; i < data.size(); i++){
            dao.update(data.get(i++), Integer.parseInt(data.get(i)));
        }
    }
}
