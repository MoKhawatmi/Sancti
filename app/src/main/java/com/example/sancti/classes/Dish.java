package com.example.sancti.classes;

public class Dish {
    String title;
    String description;
    String image;

    public Dish(String title, String description) {
        this.title = title;
        this.description = description;
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
}
