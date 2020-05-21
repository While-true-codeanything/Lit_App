package com.example.litapp.MainActivity.DataClasses;

public class Task {
    private String text;
    private String ref;
    private boolean Plugged;
    public Task(String text,String ref,boolean Plugged){
        this.text=text;
        this.ref=ref;
        this.Plugged=Plugged;
    }

    public String getText() {
        return text;
    }

    public String getRef() {
        return ref;
    }

    public boolean isPlugged() {
        return Plugged;
    }
}
