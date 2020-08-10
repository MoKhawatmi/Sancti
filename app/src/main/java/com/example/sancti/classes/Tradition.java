package com.example.sancti.classes;

public class Tradition {
    String image;
    String title;
    String description;

    public Tradition(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
