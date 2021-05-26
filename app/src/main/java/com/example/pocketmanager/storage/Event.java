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
    int priority;

    LocationData location = new LocationData();
    ArrayList<WeatherData> eventWeather;


    public static final int PRIORITY_HIGH = 0;
    public static final int PRIORITY_MEDIUM = 0;
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_TRANS = 0;



    private Event(String name, Time startTime, Time endTime, LocationData location, String description, int priority) {
        this.eventName = new String(name);
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = new String(description);
        this.priority = priority;
    }

    public static boolean addEvent(String name, Time startTime, Time endTime, LocationData location, String description, int priority){
        Calendar cal = Calendar.getInstance();
        Event new_event = new Event(name, startTime, endTime, location, description, priority);

        //이벤트가 끝나지 않았거나 시작하지 않은 경우
        if (cal.getTimeInMillis() / 1000 < endTime.getDt()) {
            ListIterator<Event> it = upcomingEvents.listIterator();
            Event event_node;
            while (it.hasNext()) {
                event_node = it.next();
                if (event_node.startTime.getDt() < endTime.getDt())
                    break;
            }
            if (it.hasPrevious())
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
            if (it.hasPrevious())
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
    public static boolean addEvent(String name, Time startTime, Time endTime, LocationData location, String description) {
        return addEvent(name, startTime, endTime, location, description, PRIORITY_MEDIUM);
    }
    public static boolean addEvent(String name, Time startTime, Time endTime, LocationData location, int priority) {
        return addEvent(name, startTime, endTime, location, "", priority);
    }
    public static boolean addEvent(String name, Time startTime, Time endTime, LocationData location) {
        return addEvent(name, startTime, endTime, location, "", PRIORITY_MEDIUM);
    }

    public static boolean removeEvent(Event event) {
        if (upcomingEvents.remove(event))
            return true;
        else
            return pastEvents.remove(event);
    }

    /** Getter **/
    public String getEventName() {
        return eventName;
    }
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }
}
