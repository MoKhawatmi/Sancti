package com.example.sancti.classes;

import java.util.Random;

public class Highlight {
    int id;
    String title;
    String description;
    String image;

    public Highlight(int id, String title, String description,String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public Highlight(String title, String description,String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
