package com.example.sancti.classes;

public class Flight {

    String name;
    String depTime;
    String destination;
    String time;
    String price;
    String airLine;
    int dbId;

    public Flight(String name,String depTime,String airLine,String destination, String time, String price) {
        this.name = name;
        this.depTime=depTime;
        this.destination = destination;
        this.time = time;
        this.price = price;
        this.airLine=airLine;
    }

    public Flight(String name,String depTime,String airLine,String destination, String time, String price, int dbId) {
        this.name = name;
        this.depTime=depTime;
        this.destination = destination;
        this.time = time;
        this.price = price;
        this.airLine=airLine;
        this.dbId=dbId;
    }

    public String getName() {
        return name;
    }

    public String getDepTime(){
      return depTime;
    };

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

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }
}
