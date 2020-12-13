package com.example.sancti.classes;

import java.util.Random;

public class Highlight {
    int id;
    String title;
    String description;
    String image;
    int isLog;


    public Highlight(int id, String title, String description,String image,int isLog) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        if(isLog==1){
            this.isLog=1;
        }else{
            this.isLog=0;
        }

    }

    public Highlight(String title, String description,String image, int isLog) {
        this.title = title;
        this.description = description;
        this.image = image;
        if(isLog==1){
            this.isLog=1;
        }else{
            this.isLog=0;
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsLog() {
        return isLog;
    }

    public void setLog(int log) {
        isLog = log;
    }
}
