package com.example.sancti.classes;

public class Flight {

    String name;
    String destination;
    String time;
    String price;
    String airLine;

    public Flight(String name,String airLine,String destination, String time, String price) {
        this.name = name;
        this.destination = destination;
        this.time = time;
        this.price = price;
        this.airLine=airLine;
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

    public String getPrice() {
        return price;
    }

    public String getAirLine() {
        return airLine;
    }
}
