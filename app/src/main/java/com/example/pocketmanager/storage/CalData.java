package com.example.pocketmanager.storage;

import java.util.Date;

public class CalData {
    private Date date;
    private int day, dayOfWeek;

    public CalData(Date date) {
        this.date = date;
    }

    public CalData(int day, int dayOfWeek) {
        this.day = day;
        this.dayOfWeek = dayOfWeek;
    }

    public Date getDate() { return date; }
    public int getDay() { return day; }
    public int getDayOfWeek() { return dayOfWeek; }
}