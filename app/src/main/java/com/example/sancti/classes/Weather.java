package com.example.sancti.classes;

public class Weather {

    String date;
    String min;
    String max;
    String condition;
    String image;

    public Weather(String date, String min, String max, String condition, String image) {
        this.date = date;
        this.min = min;
        this.max = max;
        this.condition = condition;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getCondition() {
        return condition;
    }

    public String getImage() {
        return image;
    }
}
