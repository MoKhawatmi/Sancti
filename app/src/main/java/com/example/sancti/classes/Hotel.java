package com.example.sancti.classes;

public class Hotel {
    String name;
    String location;
    double price;
    String image;

    public Hotel(String name, String location, double price) {
        this.name = name;
        this.location = location;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

}
