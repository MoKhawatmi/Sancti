package com.example.sancti.classes;

public class Flight {

    String name;
    String destination;
    String time;
    double price;
    String image;

    public Flight(String name, String destination, String time, double price) {
        this.name = name;
        this.destination = destination;
        this.time = time;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return destination;
    }

    public String getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
