package com.example.sancti.classes;

public class JournalLog {
    int id;
    String text;
    String image;
    String date;


    public JournalLog(String text,String image,String date){
        this.text=text;
        this.image=image;
        this.date=date;
    }

    public JournalLog(int id,String text,String image,String date){
        this.id=id;
        this.text=text;
        this.image=image;
        this.date=date;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public String getDate(){
        return date;
    }

    public void setText(String text) {
        this.text = text;
    }
}
