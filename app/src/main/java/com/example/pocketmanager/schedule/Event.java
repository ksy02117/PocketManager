package com.example.pocketmanager.schedule;

import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.weather.WeatherData;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

public class Event {

    //시간 역순으로 나열
    public static LinkedList<Event> pastEvents = new LinkedList<>();
    //시간순서대로 나열
    public static LinkedList<Event> upcomingEvents = new LinkedList<>();

    public static HashMap<Date, LinkedList<Event>> Events;

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
            Event event_node = null;

            while (it.hasNext()) {
                event_node = it.next();
                if (event_node.startTime.getDt() < endTime.getDt())
                    break;
            }

            //no event
            if (event_node == null){
                it.add(new_event);
                return true;
            }

            //맨끝에 저장되어야 할 경우
            if (!it.hasNext() && event_node.endTime.getDt() < startTime.getDt()) {
                it.add(new_event);
                return true;
            }

            it.previous();
            //맨 앞인 경우
            if (!it.hasPrevious()) {
                it.add(new_event);
                return true;
            }
            //가운데인 경우
            event_node = it.previous();
            if (event_node.endTime.getDt() < startTime.getDt()) {
                it.add(new_event);
                return true;
            }
            //자리가 없는 경우
            return false;

        }
        else {
            ListIterator<Event> it = pastEvents.listIterator();
            Event event_node = null;

            while (it.hasNext()) {
                event_node = it.next();
                if (event_node.endTime.getDt() > startTime.getDt())
                    break;
            }

            //no event
            if (event_node == null) {
                it.add(new_event);
                return true;
            }
            //last node
            if (!it.hasNext() && event_node.startTime.getDt() > endTime.getDt()) {
                it.add(new_event);
                return true;
            }

            it.previous();
            //first node
            if (!it.hasPrevious()) {
                it.add(new_event);
                return true;
            }
            //rest node
            event_node = it.previous();
            if (event_node.startTime.getDt() < endTime.getDt()) {
                it.add(new_event);
                return true;
            }
            //no possible location
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

    /** Getter **/
    public String getEventName() {
        return eventName;
    }
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }
}
