package com.example.pocketmanager.schedule.storage;

import android.util.Log;

import com.example.pocketmanager.map.LocationDBHelper;
import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.general.Time;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Event extends AbstractEvent implements Serializable {
    public static HashMap<Long, LinkedList<Event>> events = new HashMap<>();

    private HashMap<Long, LinkedList<SubEvent>> subEvents = new HashMap<>();


    private Event(long id, String name, Time startTime, Time endTime, LocationData location, boolean outdoor, String description, int priority) {
        super(id, name, startTime, endTime, location, outdoor, description, priority);
    }

    public static Event createEvent(String name, Time startTime, Time endTime, LocationData location, boolean outdoor, String description, int priority) {
        long id = EventDBHelper.insert(null, name, startTime, endTime, location, outdoor, description, priority);

        Event event = new Event(id, name, startTime, endTime, location, outdoor, description, priority);
        boolean output = addEvent(event);
        if (output)
            return event;
        else {
            EventDBHelper.delete(id);
            return null;
        }
    }

    public static Event loadEvent(long id, String name, Time startTime, Time endTime, LocationData location, boolean outdoor, String description, int priority) {
        Event event = new Event(id, name, startTime, endTime, location, outdoor, description, priority);
        addEvent(event);
        return event;
    }


    public static Event findEventByID(long id) {
        Iterator<Map.Entry<Long,LinkedList<Event>>> it = events.entrySet().iterator();
        while (it.hasNext()) {
            List<Event> list = it.next().getValue();
            Iterator<Event> it2 = list.iterator();
            while (it2.hasNext()) {
                Event event = it2.next();
                if (event.getID() == id)
                    return event;
            }
        }
        return null;
    }



    public static boolean addEvent(Event event) {
        long startDateID = event.getStartTime().getDateID();
        long endDateID = event.getEndTime().getDateID();
        boolean flag = true;
        //하루 이상 갈 경우를 위한 루프
        for (long i = startDateID; i <= endDateID; i++) {
            //이벤트 리스트가 없으면 만든다
            if (!events.containsKey(i))
                events.put(i, new LinkedList<>());
            //이벤트 추가
            List<Event> list = events.get(i);
            if (addEvent(list, event) == false)
                flag = false;
        }
        //충돌이 일어나면 해당 이벤트 지움
        if (flag == false) {
            for (long i = startDateID; i <= endDateID; i++) {
                List<Event> list = events.get(i);
                removeEvent(list, event);
            }
        }
        return flag;
    }
    private static boolean addEvent(List<Event> list, Event newEvent) {
        ListIterator<Event> it = list.listIterator();
        Event eventNode = null;

        while (it.hasNext()) {
            eventNode = it.next();
            if (eventNode.startTime.getDt() > newEvent.endTime.getDt())
                break;
        }

        //no event
        if (eventNode == null){
            it.add(newEvent);
            return true;
        }

        //맨끝에 저장되어야 할 경우
        if (!it.hasNext() && eventNode.endTime.getDt() <= newEvent.startTime.getDt()) {
            it.add(newEvent);
            return true;
        }
        else if(!it.hasNext() && eventNode.endTime.getDt() > newEvent.startTime.getDt()) {
            return false;
        }

        it.previous();
        //맨 앞인 경우
        if (!it.hasPrevious() && eventNode.getStartTime().getDt() >= newEvent.endTime.getDt() ) {
            it.add(newEvent);
            return true;
        }
        else if (!it.hasPrevious())
            return false;
        //가운데인 경우
        eventNode = it.previous();
        if (eventNode.endTime.getDt() <= newEvent.startTime.getDt()) {
            it.add(newEvent);
            return true;
        }
        //자리가 없는 경우
        return false;
    }



    public static boolean removeEvent(@NotNull Event event) {
        Log.d("DB", "eventDeletion");
        long startDateID = event.getStartTime().getDateID();
        long endDateID = event.getEndTime().getDateID();
        boolean flag = true;
        //하루 이상 갈 경우를 위한 루프
        for (long i = startDateID; i <= endDateID; i++) {
            //이벤트 삭제
            List<Event> list = events.get(i);
            if (removeEvent(list, event) == false)
                flag = false;
            if (list.isEmpty())
                events.remove(i);
        }
        Log.d("DB", "actualDeletion");
        EventDBHelper.delete(event.getID());
        return flag;
    }
    private static boolean removeEvent(List<Event> list, Event event) {
        Iterator<Event> it = list.iterator();
        while (it.hasNext()){
            Event tmp = it.next();
            if (tmp.getID() == event.getID())
                return list.remove(tmp);
        }
        return false;
    }


    public HashMap<Long, LinkedList<SubEvent>> getSubEvents() { return subEvents; }

}
