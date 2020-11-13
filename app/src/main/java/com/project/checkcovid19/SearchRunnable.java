package com.project.checkcovid19;

public class SearchRunnable implements Runnable {
    private String[] target;
    private int num;

    public SearchRunnable(String[] target, int num){
        this.target = target;
        this.num = num;
    }

    @Override
    public void run() {
        if(target.length > num){
            for(int i = 0; i<num;i++){
                if(target[i].equals("서울"))
                    System.out.println("서울 : " + target[i+1]);
                else if(target[i].equals("부산"))
                    System.out.println("부산 : " + target[i+1]);
                else if(target[i].equals("대구"))
                    System.out.println("대구 : " + target[i+1]);
                else if(target[i].equals("인천"))
                    System.out.println("인천 : " + target[i+1]);
                else if(target[i].equals("광주"))
                    System.out.println("광주 : " + target[i+1]);
                else if(target[i].equals("대전"))
                    System.out.println("대전 : " + target[i+1]);
                else if(target[i].equals("울산"))
                    System.out.println("울산 : " + target[i+1]);
                else if(target[i].equals("세종"))
                    System.out.println("세종 : " + target[i+1]);
                else if(target[i].equals("경기"))
                    System.out.println("경기 : " + target[i+1]);
                else if(target[i].equals("강원"))
                    System.out.println("강원 : " + target[i+1]);
                else if(target[i].equals("충북"))
                    System.out.println("충북 : " + target[i+1]);
                else if(target[i].equals("충남"))
                    System.out.println("충남 : " + target[i+1]);
                else if(target[i].equals("전북"))
                    System.out.println("전북 : " + target[i+1]);
                else if(target[i].equals("전남"))
                    System.out.println("전남 : " + target[i+1]);
                else if(target[i].equals("경북"))
                    System.out.println("경북 : " + target[i+1]);
                else if(target[i].equals("경남"))
                    System.out.println("경남 : " + target[i+1]);
                else if(target[i].equals("제주"))
                    System.out.println("제주 : " + target[i+1]);
                else if(target[i].equals("검역"))
                    System.out.println("검역 : " + target[i+1]);
            }
        }
        else {
            for (int i = num / 2; i < target.length; i++) {
                if(target[i].equals("서울"))
                    System.out.println("서울 : " + target[i+1]);
                else if(target[i].equals("부산"))
                    System.out.println("부산 : " + target[i+1]);
                else if(target[i].equals("대구"))
                    System.out.println("대구 : " + target[i+1]);
                else if(target[i].equals("인천"))
                    System.out.println("인천 : " + target[i+1]);
                else if(target[i].equals("광주"))
                    System.out.println("광주 : " + target[i+1]);
                else if(target[i].equals("대전"))
                    System.out.println("대전 : " + target[i+1]);
                else if(target[i].equals("울산"))
                    System.out.println("울산 : " + target[i+1]);
                else if(target[i].equals("세종"))
                    System.out.println("세종 : " + target[i+1]);
                else if(target[i].equals("경기"))
                    System.out.println("경기 : " + target[i+1]);
                else if(target[i].equals("강원"))
                    System.out.println("강원 : " + target[i+1]);
                else if(target[i].equals("충북"))
                    System.out.println("충북 : " + target[i+1]);
                else if(target[i].equals("충남"))
                    System.out.println("충남 : " + target[i+1]);
                else if(target[i].equals("전북"))
                    System.out.println("전북 : " + target[i+1]);
                else if(target[i].equals("전남"))
                    System.out.println("전남 : " + target[i+1]);
                else if(target[i].equals("경북"))
                    System.out.println("경북 : " + target[i+1]);
                else if(target[i].equals("경남"))
                    System.out.println("경남 : " + target[i+1]);
                else if(target[i].equals("제주"))
                    System.out.println("제주 : " + target[i+1]);
                else if(target[i].equals("검역"))
                    System.out.println("검역 : " + target[i+1]);
            }
        }
    }
}
