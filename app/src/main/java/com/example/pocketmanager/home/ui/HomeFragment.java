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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.LocationData;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.schedule.Event;
import com.example.pocketmanager.schedule.ui.EventDetailsActivity;
import com.example.pocketmanager.schedule.ui.addEveryTimeActivity;
import com.example.pocketmanager.weather.WeatherData;

import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<Event> todayEvents;
    private RelativeLayout eventLayout, weatherLayout;
    private RecyclerView timeRecycler;
    private TimelineAdapter timeAdapter;
    private Calendar mCalendar;
    private Time todayStartTime;
    private float scale;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);

        scale = getResources().getDisplayMetrics().density;
        //dpAsPixels = (int) (10 * scale + 0.5f);

        eventLayout = (RelativeLayout) view.findViewById(R.id.home_event_layout);
        weatherLayout = (RelativeLayout) view.findViewById(R.id.home_weather_layout);
        mCalendar = Calendar.getInstance();

        /*
          오늘 이벤트 가져오기




         */
        // test data
        Time startTime = new Time();
        Time endTime = new Time();
        LocationData testLocation = new LocationData();
        testLocation.setLocation(-90, 0);

        mCalendar.set(2021, 4, 28, 12, 0);
        startTime.setDt(mCalendar.getTimeInMillis() / 1000);
        mCalendar.set(2021, 4, 28, 13, 30);
        endTime.setDt(mCalendar.getTimeInMillis() / 1000);

        Event e1 = new Event("TEST1", startTime, endTime, testLocation, "TEST1 DESCRIPTION", true, 0);
        todayStartTime = new Time();
        todayStartTime.setDt(startTime.getDt());

        mCalendar.set(2021, 4, 28, 15, 0);
        startTime.setDt(mCalendar.getTimeInMillis() / 1000);
        mCalendar.set(2021, 4, 28, 16, 0);
        endTime.setDt(mCalendar.getTimeInMillis() / 1000);

        Event e2 = new Event("TEST2", startTime, endTime, testLocation, "TEST2 DESCRIPTION", true, 0);

        timeRecycler = (RecyclerView) view.findViewById(R.id.home_timeline);
        timeRecycler.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        timeRecycler.setLayoutManager(mLayoutManager);

        int endHour;
        if (endTime.getMin() == 0)
            endHour = endTime.getHour();
        else
            endHour = endTime.getHour() + 1;

        Log.d("e1.startTime", "" + e1.getStartTime().getHour());

        timeAdapter = new TimelineAdapter(getActivity(), todayStartTime.getHour(), endHour);
        timeRecycler.setAdapter(timeAdapter);

        addEvent(e1);
        //addEvent(e2);

        return view;
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
        test.setGravity(Gravity.CENTER);
        test.setBackgroundColor(parentColor);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EventDetailsActivity.class);
                intent.putExtra("eventName", e.getEventName());
                intent.putExtra("eventDescription", e.getDescription());
                intent.putExtra("startTime", e.getStartTime().getDt());
                intent.putExtra("endTime", e.getEndTime().getDt());
                startActivityForResult(intent, 1);
            }
        });

        addWeather(startHour);
        eventLayout.addView(test);
    }

    private void addWeather(int startHour) {
        ImageView newIcon;
        RelativeLayout.LayoutParams params;

        int duration = getPixel(60);
        int untilStart = getPixel((startHour - todayStartTime.getHour()) * 60);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, duration);
        params.setMargins(0, untilStart, 0, 0);

        newIcon = new ImageView(this.getContext());
        newIcon.setLayoutParams(params);
        newIcon.setImageResource(R.drawable.w_01d);

        weatherLayout.addView(newIcon);
    }

    private int getPixel(int dp) {
        return (int) (dp * scale + 0.5f);
    }
}
