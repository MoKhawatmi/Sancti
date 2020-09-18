package com.example.sancti.classes;

public class Hotel {
    String id;
    String name;
    String location;
    String price;


    public Hotel(String id,String name, String location, String price) {
        this.id=id;
        this.name = name;
        this.location = location;
        this.price = price;
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

}
