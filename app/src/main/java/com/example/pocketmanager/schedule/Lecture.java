package com.example.pocketmanager.schedule;

import android.util.Log;

public class Lecture {
    private String name;
    private String professor;
    private String time;
    private String place;

    public Lecture() {
        name = "";
        professor = "";
        time = "";
        place = "";
    }

    public Lecture(String name, String professor, String time, String place) {
        this.name = name;
        this.professor = professor;
        this.time = time;
        this.place = place;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getProfessor() {
        return professor;
    }
    public void setProfessor(String professor) {
        this.professor = professor;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    public void log() {
        /*System.out.println(name);
        System.out.println(professor);
        System.out.println(time);
        System.out.println(place);*/
        Log.d("name", name);
        Log.d("professor", professor);
        Log.d("time", time);
        Log.d("place", place);
    }
}