package com.example.pocketmanager.home.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import java.util.TimeZone;

public class HomeFragment extends Fragment {
    private List<Event> todayEvents;
    private RelativeLayout eventLayout, weatherLayout;
    private RecyclerView timeRecycler;
    private TimelineAdapter timeAdapter;
    private LinearLayout l;
    private Time todayStartTime;
    private float scale;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);

        scale = getResources().getDisplayMetrics().density;

        l = (LinearLayout) view.findViewById(R.id.home_view);
        eventLayout = (RelativeLayout) view.findViewById(R.id.home_event_layout);
        weatherLayout = (RelativeLayout) view.findViewById(R.id.home_weather_layout);

        update();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        update();
    }

    public void update() {
        int startHour, endHour;
        Time t = new Time();
        Calendar mCal = Calendar.getInstance();
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

        Time s = e.getFirst().getStartTime();
        startHour = e.getFirst().getStartTime().getHour();
        todayStartTime = new Time(s.getYear(), s.getMonth(), s.getDay(), s.getHour(), 0, s.getSec());
        endHour = e.getLast().getEndTime().getHour();

        if (e.getLast().getEndTime().getMin() != 0)
            endHour++;

        for (Event myEvent : e) {
            addEvent(myEvent);
        }

        timeRecycler = (RecyclerView) view.findViewById(R.id.home_timeline);
        timeRecycler.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        timeRecycler.setLayoutManager(mLayoutManager);
        timeAdapter = new TimelineAdapter(getActivity(), startHour, endHour);
        timeRecycler.setAdapter(timeAdapter);
    }

    private void wowSuchEmpty() {
        l = (LinearLayout) view.findViewById(R.id.home_view);
        l.setVisibility(View.GONE);
    }

    private void addEvent(Event e) {
        TextView test;
        RelativeLayout.LayoutParams params;
        int startHour, startMinute;
        int endHour, endMinute;
        int textColor, parentColor, childColor;
        int dp, pixel;
        int duration, untilStart;
        String eventName;

        startHour = e.getStartTime().getHour();
        startMinute = e.getStartTime().getMin();
        endHour = e.getEndTime().getHour();
        endMinute = e.getEndTime().getMin();
        duration = getPixel((int) (e.getEndTime().getDt() - e.getStartTime().getDt()) / 60);
        untilStart = getPixel((int) (e.getStartTime().getDt() - todayStartTime.getDt()) / 60);
        eventName = e.getEventName();
        textColor = getResources().getColor(R.color.baseTextColor);
        parentColor = getResources().getColor(R.color.parentEventRed);
        childColor = getResources().getColor(R.color.childEventGreen);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, duration);
        params.setMargins(0, untilStart, 0, 0);

        test = new TextView(this.getContext());
        test.setLayoutParams(params);
        test.setText(e.getEventName());
        test.setTextSize(16);
        test.setTextColor(Color.WHITE);
        test.setMaxLines(1);
        test.setGravity(Gravity.LEFT);
        test.setBackgroundColor(parentColor);
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
        test.setBackgroundColor(childColor);

        eventLayout.addView(test);
    }

    private void addWeather(Event e, int startHour, int endHour) {
        ImageView newIcon;
        RelativeLayout.LayoutParams params;

        int duration = getPixel(60);
        int untilStart = getPixel((startHour - todayStartTime.getHour()) * 60);

        if (e.getEventWeather().isEmpty())
            return;

        for (int i = 0; i < endHour - startHour; i++) {
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, duration);
            params.setMargins(0, untilStart + getPixel(i * 60), 0, 0);

            newIcon = new ImageView(this.getContext());
            newIcon.setLayoutParams(params);

            String icon;
            int resourceId = R.drawable.w_01d;

            icon = e.getEventWeather().get(i).getIcon();
            resourceId = getResources().getIdentifier("w_" + icon.replace('n', 'd'), "drawable", getContext().getPackageName());

            newIcon.setImageResource(resourceId);
            weatherLayout.addView(newIcon);
        }
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
            if (!event.isOutdoor())
                continue;
            if (Math.abs(event.getLocation().getLatitude() - lats.get(lats.size() - 1)) < 0.5  && Math.abs(event.getLocation().getLongitude() - lats.get(lats.size() - 1)) < 0.5)
                continue;
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
