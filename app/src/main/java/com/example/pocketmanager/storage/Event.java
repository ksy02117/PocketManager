package com.example.pocketmanager.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.ListIterator;

public class Event {

    //시간 역순으로 나열
    public static LinkedList<Event> pastEvents = new LinkedList<>();
    //시간순서대로 나열
    public static LinkedList<Event> upcomingEvents = new LinkedList<>();

    private Time startTime;
    private Time endTime;
    String eventName;
    String description;
    boolean outdoor;
    int priority;

    LocationData location = new LocationData();
    ArrayList<WeatherData> eventWeather;


    public static final int PRIORITY_HIGH = 0;
    public static final int PRIORITY_MEDIUM = 0;
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_TRANS = 0;



    private Event(String name, Time startTime, Time endTime, LocationData location, String description, boolean outdoor, int priority) {
        this.eventName = new String(name);
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = new String(description);
        this.outdoor = outdoor;
        this.priority = priority;
    }

    public static boolean addEvent(String name, Time startTime, Time endTime, LocationData location, String description, boolean outdoor, int priority){
        long dt = Time.getCurrentDt();
        Event new_event = new Event(name, startTime, endTime, location, description, outdoor, priority);

        //이벤트가 끝나지 않았거나 시작하지 않은 경우
        if (dt < endTime.getDt()) {
            ListIterator<Event> it = upcomingEvents.listIterator();
            Event event_node;
            while (it.hasNext()) {
                event_node = it.next();
                if (event_node.startTime.getDt() < endTime.getDt())
                    break;
            }
            it.previous();
            if (!it.hasPrevious()) {
                it.add(new_event);
                return true;
            }
            event_node = it.previous();
            if (event_node.endTime.getDt() < startTime.getDt()) {
                it.add(new_event);
                return true;
            }
            return false;

        }
        else {
            ListIterator<Event> it = pastEvents.listIterator();
            Event event_node;
            while (it.hasNext()) {
                event_node = it.next();
                if (event_node.endTime.getDt() > startTime.getDt())
                    break;
            }
            it.previous();
            if (!it.hasPrevious()) {
                it.add(new_event);
                return true;
            }
            event_node = it.previous();
            if (event_node.startTime.getDt() < endTime.getDt()) {
                it.add(new_event);
                return true;
            }
            return false;
        }
    }
    public static boolean addEvent(String name, Time startTime, Time endTime, LocationData location, String description, boolean outdoor) {
        return addEvent(name, startTime, endTime, location, description, outdoor, PRIORITY_MEDIUM);
    }
    public static boolean addEvent(String name, Time startTime, Time endTime, LocationData location, boolean outdoor, int priority) {
        return addEvent(name, startTime, endTime, location, "", outdoor, priority);
    }
    public static boolean addEvent(String name, Time startTime, Time endTime, LocationData location, boolean outdoor) {
        return addEvent(name, startTime, endTime, location, "", outdoor, PRIORITY_MEDIUM);
    }
    public static boolean addEvent(String name, Time startTime, Time endTime, String description, int priority) {
        return addEvent(name, startTime, endTime, null, description, false, priority);
    }
    public static boolean addEvent(String name, Time startTime, Time endTime, String description) {
        return addEvent(name, startTime, endTime, null, description, false, PRIORITY_MEDIUM);
    }
    public static boolean addEvent(String name, Time startTime, Time endTime, int priority) {
        return addEvent(name, startTime, endTime, null, "", false, priority);
    }
    public static boolean addEvent(String name, Time startTime, Time endTime) {
        return addEvent(name, startTime, endTime, null, "", false, PRIORITY_MEDIUM);
    }



    public static boolean removeEvent(Event event) {
        if (upcomingEvents.remove(event))
            return true;
        else
            return pastEvents.remove(event);
    }
}
