package com.example.pocketmanager.schedule.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.CalData;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.home.ui.TimelineAdapter;
import com.example.pocketmanager.schedule.storage.Event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

public class MyPagerAdapter2 extends PagerAdapter {
    private RecyclerView dateView, timelineView, scheduleView;
    private Context mContext;
    private CalendarAdapter2 adapter;
    private ArrayList<CalData> arrData;
    private Calendar mCal;
    private View mView;
    private int thisDay, thisWeek, thisMonth, thisYear;

    public MyPagerAdapter2(Context context) {
        super();
        mContext = context;
        mCal = Calendar.getInstance();
        thisDay = mCal.get(Calendar.DAY_OF_YEAR);
        thisWeek = mCal.get(Calendar.WEEK_OF_YEAR);
        thisMonth = mCal.get(Calendar.MONTH);
        thisYear = mCal.get(Calendar.YEAR);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPos = position - 100;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.calendar_week_view, container, false);

        mCal.set(Calendar.YEAR, thisYear);
        mCal.set(Calendar.DAY_OF_YEAR, thisDay + realPos * 3);

        setTimeline(0, 23);
        setCalendarDate();
        setDisplaySchedule();
        ((ViewPager) container).addView(mView);

        return mView;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return 200;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return (view == object);
    }

    private void setCalendarDate(){
        arrData = new ArrayList<>();

        if (Event.events.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                arrData.add(new CalData(mCal.getTime()));
                mCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        else {
            Time t = new Time();
            for (int i = 0; i < 3; i++) {
                LinkedList<Event> e;
                LinkedList<Event> events = new LinkedList<>();
                t.setDt(mCal.getTimeInMillis() / 1000);
                long currentID = t.getDateID();

                e = Event.events.get(currentID);

                if (e == null) {
                    arrData.add(new CalData(mCal.getTime()));
                    mCal.add(Calendar.DAY_OF_MONTH, 1);
                    continue;
                }

                Iterator<Event> list = e.iterator();
                while (list.hasNext()) {
                    Event event = list.next();
                    if (event.getPriority() == Event.PRIORITY_TRANS)
                        continue;
                    events.add(event);
                }

                arrData.add(new CalData(mCal.getTime(), events));
                mCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        adapter = new CalendarAdapter2(mView.getContext(), arrData);

        dateView = (RecyclerView) mView.findViewById(R.id.calendar_date_view);
        dateView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        dateView.setLayoutManager(mLayoutManager);

        dateView.setId(mCal.get(Calendar.MONTH));
        dateView.setAdapter(adapter);
    }

    private void setTimeline(int startTime, int endTime) {
        timelineView = (RecyclerView) mView.findViewById(R.id.calendar_week_time_view);
        timelineView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // 기본값이 VERTICAL
        timelineView.setLayoutManager(mLayoutManager);

        TimelineAdapter tAdapter = new TimelineAdapter(this.mContext, startTime, endTime);
        timelineView.setAdapter(tAdapter);
    }

    private void setDisplaySchedule() {
        scheduleView = (RecyclerView) mView.findViewById(R.id.calendar_schedule_view);
        scheduleView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        scheduleView.setLayoutManager(mLayoutManager);

        DisplayScheduleAdapter tAdapter = new DisplayScheduleAdapter(this.mContext, arrData);
        scheduleView.setAdapter(tAdapter);
    }
}
