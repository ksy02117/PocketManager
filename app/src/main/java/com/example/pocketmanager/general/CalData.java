package com.example.pocketmanager.general;

import com.example.pocketmanager.schedule.Event;

import java.util.ArrayList;
import java.util.Date;

public class CalData {
    private Date date;
    private ArrayList<Event> events;

    public CalData(Date date) {
        this.date = date;
    }
    public CalData(Date date, ArrayList<Event> events) {
        this.date = date;
        this.events = events;
    }

    public Date getDate() { return date; }
    public ArrayList<Event> getEvents() { return events; }
}