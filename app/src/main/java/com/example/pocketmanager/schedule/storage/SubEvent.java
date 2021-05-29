package com.example.pocketmanager.schedule.storage;

import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.general.Time;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SubEvent extends AbstractEvent implements Serializable {
    public static Event parent;



    private SubEvent(long id, Event parent, String name, Time startTime, Time endTime, LocationData location, boolean outdoor, String description, int priority) {
        super(id, name, startTime, endTime, location, outdoor, description, priority);
        this.parent = parent;
    }
    public static SubEvent createSubEvent(Event parent, String name, Time startTime, Time endTime, LocationData location, boolean outdoor, String description, int priority) {
        long id = EventDBHelper.insert(parent, name, startTime, endTime, location, outdoor, description, priority);

        SubEvent event = new SubEvent(id, parent, name, startTime, endTime, location, outdoor, description, priority);
        addEvent(event);
        return event;
    }
    public static SubEvent loadEvent(long id, Event parent, String name, Time startTime, Time endTime, LocationData location, boolean outdoor, String description, int priority) {
        SubEvent event = new SubEvent(id, parent, name, startTime, endTime, location, outdoor, description, priority);
        addEvent(event);

        return event;
    }



    public static boolean addEvent(SubEvent event) {
        long parentStartDateID = event.parent.getStartTime().getDateID();
        long startDateID = event.getStartTime().getDateID();
        long parentEndDateID = event.parent.getEndTime().getDateID();
        long endDateID = event.getEndTime().getDateID();

        boolean flag = true;

        //부 이벤트가 이벤트안에 못 들어갈 경우
        if (parentStartDateID > startDateID || parentEndDateID < endDateID)
            return false;

        HashMap<Long, LinkedList<SubEvent>> subEvents = parent.getSubEvents();
        //하루 이상 갈 경우를 위한 루프
        for (long i = startDateID; i <= endDateID; i++) {
            //이벤트 리스트가 없으면 만든다
            if (!subEvents.containsKey(i))
                subEvents.put(i, new LinkedList<>());
            //이벤트 추가
            List<SubEvent> list = subEvents.get(i);
            if (addEvent(list, event) == false)
                flag = false;
        }
        //충돌이 일어나면 해당 이벤트 지움
        if (flag == false) {
            for (long i = startDateID; i <= endDateID; i++) {
                List<SubEvent> list = subEvents.get(i);
                removeEvent(list, event);
            }
        }
        return flag;
    }
    private static boolean addEvent(List<SubEvent> list, SubEvent newEvent) {
        ListIterator<SubEvent> it = list.listIterator();
        SubEvent eventNode = null;

        while (it.hasNext()) {
            eventNode = it.next();
            if (eventNode.startTime.getDt() < newEvent.endTime.getDt())
                break;
        }

        //no event
        if (eventNode == null){
            it.add(newEvent);
            return true;
        }

        //맨끝에 저장되어야 할 경우
        if (!it.hasNext() && eventNode.endTime.getDt() < newEvent.startTime.getDt()) {
            it.add(newEvent);
            return true;
        }

        it.previous();
        //맨 앞인 경우
        if (!it.hasPrevious()) {
            it.add(newEvent);
            return true;
        }
        //가운데인 경우
        eventNode = it.previous();
        if (eventNode.endTime.getDt() < newEvent.startTime.getDt()) {
            it.add(newEvent);
            return true;
        }
        //자리가 없는 경우
        return false;
    }


    public static boolean removeEvent(SubEvent event) {
        long startDateID = event.getStartTime().getDateID();
        long endDateID = event.getEndTime().getDateID();
        boolean flag = true;

        HashMap<Long, LinkedList<SubEvent>> subEvents = event.parent.getSubEvents();
        //하루 이상 갈 경우를 위한 루프
        for (long i = startDateID; i <= endDateID; i++) {
            //이벤트 삭제
            List<SubEvent> list = subEvents.get(i);
            if (removeEvent(list, event) == false)
                flag = false;
            if (list.isEmpty())
                subEvents.remove(i);
        }
        EventDBHelper.delete(event.getID());
        return flag;
    }
    private static boolean removeEvent(List<SubEvent> list, SubEvent event) {
        Iterator<SubEvent> it = list.iterator();
        while (it.hasNext()){
            SubEvent tmp = it.next();
            if (tmp.getID() == event.getID())
                return list.remove(tmp);
        }
        return false;
    }


}
