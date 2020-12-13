package com.example.sancti.classes;

public class Hotel {
    String id;
    String name;
    String location;
    String price;
    int dbId;

    public Hotel(String id,String name, String location, String price) {
        this.id=id;
        this.name = name;
        this.location = location;
        this.price = price;
    }


    public Hotel(String id,String name, String location, String price, int dbId) {
        this.id=id;
        this.name = name;
        this.location = location;
        this.price = price;
        this.dbId=dbId;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }
}
