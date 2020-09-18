package com.example.sancti.classes;

public class Trip {
    int id;
    String title;

    public Trip(String title){
        this.title=title;
    }

    public Trip(int id,String title){
        this.id=id;
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
}
