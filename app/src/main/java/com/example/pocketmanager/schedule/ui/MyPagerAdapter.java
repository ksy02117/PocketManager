package com.example.pocketmanager.schedule.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.CalData;
import com.example.pocketmanager.schedule.storage.Event;
import com.example.pocketmanager.general.Time;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.TimeZone;

public class MyPagerAdapter extends PagerAdapter {
    private GridView mGridView;
    private CalendarAdapter adapter;
    private ArrayList<CalData> arrData;
    private Context mContext;
    private View mView;
    private Time currentTime;
    private Time day1;

    public MyPagerAdapter(Context context) {
        super();
        mContext = context ;
        currentTime = new Time();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPos = position - 100;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.calendar_grid, container, false);

        day1 = new Time(currentTime.getYear(), currentTime.getMonth() + realPos, 1, 0, 0, 0);

        setCalendarDate();
        ((ViewPager) container).addView(mGridView);

        return mGridView;
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

        Calendar p = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
        p.set(day1.getYear(), day1.getMonth() - 1, day1.getDay());
        int startDay = p.get(Calendar.DAY_OF_WEEK);

        // move calendar backwards to the beginning of the week
        p.add(Calendar.DAY_OF_MONTH, -startDay + 1);

        // 이벤트가 없을 경우
        if (Event.events.isEmpty()) {
            for (int i = 0; i < 42; i++) {
                arrData.add(new CalData(p.getTime()));
                p.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        else {
            Time t = new Time();

            for (int i = 0; i < 42; i++) {
                LinkedList<Event> e;
                t.setDt(p.getTimeInMillis() / 1000);
                long currentID = t.getDateID();
                e = Event.events.get(currentID);

                if (e == null) {
                    arrData.add(new CalData(p.getTime()));
                    p.add(Calendar.DAY_OF_MONTH, 1);
                    continue;
                }

                arrData.add(new CalData(p.getTime(), e));
                p.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        adapter = new CalendarAdapter(mView.getContext(), arrData);
        mGridView = (GridView) mView.findViewById(R.id.calendar_grid);
        mGridView.setId(currentTime.getMonth() + 1);
        mGridView.setAdapter(adapter);
    }
}
