package com.project.checkcovid19.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Rankings {
    private String today;
    private int rank;
    private int current_num_of_data;
    private int rank_between;
    private String path;
    private File file;
    private ArrayList<String> text;
    private boolean isExist;
    private final int day = 0;
    private final int the_area = 1;
    private final int patient = 2;
    private final int max = 18;

    public Rankings(String path){
        this.path = path+"/log.txt";
        file = new File(path);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd");
        this.today = format.format(date.getTime() + (long)(1000*60*60*9));
        text = new ArrayList<>();
        loadData();
    }

    public void saveLog(String area, int numOfPatient){
        if(!checkData(area, numOfPatient)){
            int savePoint = 0;
            if(!isExist) {
                if (current_num_of_data >= max) {
                    savePoint = current_num_of_data % max;
                    text.set(savePoint + day, today);
                    text.set(savePoint + the_area, area);
                    text.set(savePoint + patient, numOfPatient + "");
                } else {
                    text.add(today);
                    text.add(area);
                    text.add(numOfPatient + "");
                }
                current_num_of_data += 3;
            }
        }
    }

    public boolean checkData(String area, int numOfPatient){
        rank = 1;
        rank_between = 1;
        isExist = false;
        if(current_num_of_data != 0) {
            for (int i = 0; i < text.size(); i+=3) {
                if(text.get(i+day).equals(today) && text.get(i+the_area).equals(area)
                        && (numOfPatient == Integer.parseInt(text.get(i+patient)))){
                    rank_between--;
                    isExist = true;
                }
                rank_between++;
                if(numOfPatient < Integer.parseInt(text.get(i+patient)))
                    rank++;
            }
        }
        return isExist;
    }

    private boolean loadData(){
        current_num_of_data = 0;
        rank = 1;
        boolean isExist = false;

        if(!file.mkdir() && new File(path).exists()){
            BufferedReader bufferedReader = null;
            String line;
            try{
                bufferedReader = new BufferedReader(new FileReader(path));
                while((line = bufferedReader.readLine()) != null){
                    String[] data = line.split(" ");
                    for(int i = 0; i < data.length; i++) {
                        text.add(data[i]);
                        current_num_of_data ++;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(isExist == true)
                return false;
            else
                return true;
        }
        else{
            return true;
        }
    }

    public void closeFile(){

        int savePoint = current_num_of_data % max;
        ArrayList<String> save_text = new ArrayList<>();
        if (current_num_of_data >= max) {
            for(int i = 0; i < max; i+=3) {
                save_text.add(text.get((savePoint + i)%max + day));
                save_text.add(text.get((savePoint + i)%max + the_area));
                save_text.add(text.get((savePoint + i)%max + patient));

                System.out.println(i+"번째 오래된 데이터 : " +text.get((savePoint + i)%max+the_area));
            }
        }
        else {
            for(int i = text.size()-1; i > 0; i -=3) {
                save_text.add(text.get(i - patient));
                save_text.add(text.get(i - the_area));
                save_text.add(text.get(i - day));
            }
        }
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(path));
            for(int i = 0; i < save_text.size(); i+=3) {
                bufferedWriter.write(save_text.get(i+day) + " " + save_text.get(i+the_area) + " " + save_text.get(i+patient));
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(bufferedWriter != null){
                try{
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getCurrent_num_of_data(){
        return rank_between;
    }

    public int getRank(){
        return rank;
    }
}
