package com.example.pocketmanager.home.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.schedule.alarm.Alarm;
import com.example.pocketmanager.schedule.storage.Event;
import com.example.pocketmanager.schedule.storage.SubEvent;
import com.example.pocketmanager.schedule.ui.EventDetailsActivity;
import com.example.pocketmanager.transportation.PathInfoManager;
import com.example.pocketmanager.transportation.ShortestPath;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private List<Event> todayEvents;
    private RelativeLayout eventLayout, weatherLayout;
    private RecyclerView timeRecycler;
    private TimelineAdapter timeAdapter;
    private TextView emptyText;
    private LinearLayout l;
    private Calendar mCal;
    private Time todayStartTime;
    private float scale;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);

        scale = getResources().getDisplayMetrics().density;

        l = (LinearLayout) view.findViewById(R.id.home_view);
        eventLayout = (RelativeLayout) view.findViewById(R.id.home_event_layout);
        weatherLayout = (RelativeLayout) view.findViewById(R.id.home_weather_layout);
        emptyText = (TextView) view.findViewById(R.id.wow_such_empty);

        update();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        update();
    }

    public void update() {
        Time t = new Time();
        mCal = Calendar.getInstance();
        t.setDt(mCal.getTimeInMillis() / 1000);
        long id = t.getDateID();


        eventLayout.removeAllViews();
        weatherLayout.removeAllViews();

        LinkedList<Event> e = Event.events.get(id);
        if (e == null) {
            wowSuchEmpty();
            return;
        }
        addTransportationEvent();

        l.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);

        timeRecycler = (RecyclerView) view.findViewById(R.id.home_timeline);
        timeRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        timeRecycler.setLayoutManager(mLayoutManager);

        setStartDuration(e.getFirst().getStartTime(), e.getLast().getEndTime());

        for (Event myEvent : e)
            addEvent(myEvent);
    }

    private void wowSuchEmpty() {
        l = (LinearLayout) view.findViewById(R.id.home_view);
        l.setVisibility(View.GONE);
        emptyText.setVisibility(View.VISIBLE);
    }

    private void addEvent(Event e) {
        TextView test;
        RelativeLayout.LayoutParams params;
        Time startTime, endTime;
        int startHour, startMinute;
        int endHour, endMinute;
        int parentColor;
        int duration, untilStart;
        String eventName;
        long startDt, endDt;

        startTime = e.getStartTime();
        endTime = e.getEndTime();
        startHour = e.getStartTime().getHour();
        startMinute = e.getStartTime().getMin();
        endHour = e.getEndTime().getHour();
        endMinute = e.getEndTime().getMin();
        startDt = e.getStartTime().getDt();
        endDt = e.getEndTime().getDt();
        eventName = e.getEventName();

        duration = (int) (endDt - startDt) / 60;
        untilStart = (int) (startDt - todayStartTime.getDt()) / 60;

        // 오늘이 startTime
        if (startTime.getYear() == mCal.get(Calendar.YEAR) &&
                startTime.getMonth() == mCal.get(Calendar.MONTH) + 1 &&
                startTime.getDay() == mCal.get(Calendar.DAY_OF_MONTH)) {
            if (untilStart + duration >= (24 - todayStartTime.getHour()) * 60)
                duration = (24 - todayStartTime.getHour()) * 60 - untilStart;
        }
        // 오늘이 endTime
        else if (endTime.getYear() == mCal.get(Calendar.YEAR) &&
                endTime.getMonth() == mCal.get(Calendar.MONTH) + 1 &&
                endTime.getDay() == mCal.get(Calendar.DAY_OF_MONTH)) {
            untilStart = 0;
            if (untilStart + duration >= (24 - todayStartTime.getHour()) * 60) {
                duration = endHour * 60 + endMinute;
            }

        }
        else {
            untilStart = 0;
            duration = 24;
        }

        parentColor = getResources().getColor(R.color.parentEventRed);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getPixel(duration));
        params.setMargins(0, getPixel(untilStart), 0, 0);

        test = new TextView(this.getContext());
        test.setLayoutParams(params);
        test.setText(eventName);
        test.setTextSize(16);
        test.setTextColor(Color.WHITE);
        test.setMaxLines(1);
        test.setGravity(Gravity.LEFT);
        test.setPadding(getPixel(10), 0, 0, 0);
        test.setBackgroundColor(parentColor);

        if (e.getPriority() == Event.PRIORITY_TRANS) {
            String station = "null"; // 첫 차 역 이름
            int firstTrainTime = 0;  // 첫 차 시간 (단위: 분)
            int padding = firstTrainTime - untilStart;
            int transColor = getResources().getColor(R.color.parentEventTrans);

            test.setPadding(0, padding, 0, 0);
            test.setText(station);
            test.setBackgroundColor(transColor);

            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getPixel(4));
            params.setMargins(0, getPixel(firstTrainTime), 0, 0);
            View line = new View(this.getContext());
            line.setBackgroundResource(R.drawable.line);

            eventLayout.addView(line);
        }
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EventDetailsActivity.class);
                intent.putExtra("event", e);
                startActivityForResult(intent, 1);
            }
        });

        if (e.getEndTime().getMin() != 0)
            endHour += 1;


        addWeather(e, startHour, endHour);
        eventLayout.addView(test);
        for (Map.Entry<Long, LinkedList<SubEvent>> longLinkedListEntry : e.getSubEvents().entrySet()) {
            List<SubEvent> list = longLinkedListEntry.getValue();
            for (SubEvent event : list) {
                addSubEvent(event);
            }
        }
    }

    private void addSubEvent(SubEvent e) {
        TextView test;
        RelativeLayout.LayoutParams params;
        int startHour, endHour;
        int childColor;
        int leftMargin;
        int duration, untilStart;

        startHour = e.getStartTime().getHour();
        endHour = e.getEndTime().getHour();
        if (e.getEndTime().getMin() != 0)
            endHour += 1;

        duration = getPixel((int) (e.getEndTime().getDt() - e.getStartTime().getDt()) / 60);
        untilStart = getPixel((int) (e.getStartTime().getDt() - todayStartTime.getDt()) / 60);
        leftMargin = getPixel(10);
        childColor = getResources().getColor(R.color.childEventGreen);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, duration);
        params.setMargins(leftMargin, untilStart, 0, 0);

        test = new TextView(this.getContext());
        test.setLayoutParams(params);
        test.setText(e.getEventName());
        test.setTextSize(16);
        test.setTextColor(Color.WHITE);
        test.setMaxLines(1);
        test.setGravity(Gravity.RIGHT);
        test.setPadding(0, 0, getPixel(10), 0);
        test.setBackgroundColor(childColor);

        eventLayout.addView(test);
    }

    private void addWeather(Event e, int startHour, int endHour) {
        ImageView newIcon;
        RelativeLayout.LayoutParams params;

        int duration = getPixel(60);
        int untilStart = getPixel((startHour - todayStartTime.getHour()) * 60);

        if (e.getEventWeather() == null)
            return;

        for (int i = 0; i < endHour - startHour; i++) {
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, duration);
            params.setMargins(0, untilStart + getPixel(i * 60), 0, 0);

            newIcon = new ImageView(this.getContext());
            newIcon.setLayoutParams(params);

            String icon;
            int resourceId = R.drawable.w_01d;

            if (e.getEventWeather().size() != 0) {
                icon = e.getEventWeather().get(i).getIcon();
                resourceId = getResources().getIdentifier("w_" + icon.replace('n', 'd'), "drawable", getContext().getPackageName());

                newIcon.setImageResource(resourceId);
                weatherLayout.addView(newIcon);
            }
        }
    }

    private void setStartDuration(Time startTime, Time endTime) {
        int start = startTime.getHour();
        int duration = (int) (endTime.getDt() - startTime.getDt()) / 60 / 60;
        if (endTime.getMin() != 0)
            duration += 1;

        // 오늘이 startTime
        if (startTime.getYear() == mCal.get(Calendar.YEAR) &&
                startTime.getMonth() == mCal.get(Calendar.MONTH) + 1 &&
                startTime.getDay() == mCal.get(Calendar.DAY_OF_MONTH)) {
            todayStartTime = new Time(startTime.getYear(), startTime.getMonth(), startTime.getDay(), startTime.getHour(), 0, 0);
            if (start + duration >= 24)
                duration = 24 - start;
        }
        // 오늘이 endTime
        else if (endTime.getYear() == mCal.get(Calendar.YEAR) &&
                endTime.getMonth() == mCal.get(Calendar.MONTH) + 1 &&
                endTime.getDay() == mCal.get(Calendar.DAY_OF_MONTH)) {
            todayStartTime = new Time(endTime.getYear(), endTime.getMonth(), endTime.getDay(), 0, 0, 0);
            if (start + duration >= 24) {
                start = 0;
                duration = endTime.getHour();
                if (endTime.getMin() != 0)
                    duration += 1;
            }
        }
        else {
            todayStartTime = new Time(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), mCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            start = 0;
            duration = 24;
        }
        timeAdapter = new TimelineAdapter(getActivity(), start, duration);
        timeRecycler.setAdapter(timeAdapter);
    }

    private int getPixel(int dp) {
        return (int) (dp * scale + 0.5f);
    }

    private void addTransportationEvent() {
        Time t = new Time();
        List<Event> list = Event.events.get(t.getDateID());
        PathInfoManager pm = new PathInfoManager();

        ArrayList<Double> lats = new ArrayList<Double>();
        ArrayList<Double> lons = new ArrayList<Double>();
        ArrayList<Event> events = new ArrayList<>();

        lats.add(LocationData.getCurrentLocation().getLatitude());
        lons.add(LocationData.getCurrentLocation().getLongitude());
        events.add(null);
        for (int i = 0; i < list.size(); i++) {
            Event event = list.get(i);
            if (!event.isOutdoor() || event.getLocation() == null)
                continue;
            if (Math.abs(event.getLocation().getLatitude() - lats.get(lats.size() - 1)) < 0.5  && Math.abs(event.getLocation().getLongitude() - lats.get(lats.size() - 1)) < 0.5)
                continue;
            if (event.getPriority() == Event.PRIORITY_TRANS)
                return;
            lats.add(event.getLocation().getLatitude());
            lons.add(event.getLocation().getLongitude());
            events.add(event);
        }


        try {
            for (int i = 0; i < lats.size() - 1; i++) {
                double startLat = lats.get(i);
                double startLon = lons.get(i);
                double endLat = lats.get(i + 1);
                double endLon = lons.get(i + 1);
                Event event = events.get(i + 1);

                String startLoc = startLat + ", " + startLon;
                String endLoc = endLat + ", " + endLon;

                pm.setOrigin(startLoc);
                pm.setDestination(endLoc);

                ShortestPath sp = pm.getShortestPathInfo();

                long duration = Long.parseLong(sp.getDurationValue());
                Time endTime = new Time(event.getStartTime().getDt() - 1);
                Time startTime = new Time(endTime.getDt() - duration);
                if (Event.createEvent("교통", startTime, endTime, event.getLocation(), true, "교통", Event.PRIORITY_TRANS) == null) {
                    startTime = new Time(list.get(list.indexOf(event) - 1).getEndTime().getDt() + 1);
                    Event.createEvent("교통", startTime, endTime,event.getLocation(),true, "시간이 충분하지 않아요", Event.PRIORITY_TRANS);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Iterator<Event> it = list.iterator();
        while (it.hasNext()){
            Event event= it.next();
            if (event.getPriority() == Event.PRIORITY_TRANS)
                continue;
            Time departTime = new Time(event.getEndTime().getDt() - 300);
            String departTimeString = departTime.getYear()+"-"+departTime.getMonth()+"-"+departTime.getDay()+" "+departTime.getHour()+":"+departTime.getMin()+":"+departTime.getSec();
            new Alarm(this.getContext());
            Alarm.departAlarm(departTimeString);
        }

    }

}
