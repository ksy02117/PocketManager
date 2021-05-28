package com.example.pocketmanager.general;

import com.example.pocketmanager.schedule.storage.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class CalData {
    private Date date;
    private LinkedList<Event> events;

    public CalData(Date date) {
        this.date = date;
    }
    public CalData(Date date, LinkedList<Event> events) {
        this.date = date;
        this.events = events;
    }

    public Date getDate() { return date; }
    public LinkedList<Event> getEvents() { return events; }
}