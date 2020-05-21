package com.example.litapp.MainActivity.DataClasses;

public class Subject {
    private String ref;
    private String tname;
    private String subject;

    public Subject(String ref, String tname, String subject) {
        this.ref = ref;
        this.tname = tname;
        this.subject = subject;
    }

    public String getRef() {
        return ref;
    }

    public String getSubject() {
        return subject;
    }

    public String getTname() {
        return tname;
    }
}
