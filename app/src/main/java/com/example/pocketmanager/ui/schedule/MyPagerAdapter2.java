package com.example.pocketmanager.ui.schedule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.pocketmanager.R;
import com.example.pocketmanager.storage.CalData;
import com.example.pocketmanager.storage.Event;
import com.example.pocketmanager.storage.Time;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;

public class MyPagerAdapter2 extends PagerAdapter {
    private RecyclerView mRecyclerView;
    private Context mContext;
    private CalendarAdapter2 adapter;
    private ArrayList<CalData> arrData;
    private Calendar mCal, mToday;
    private View mView;
    private int thisWeek, thisMonth, thisYear;

    public MyPagerAdapter2(Context context) {
        super();
        mContext = context ;
        mCal = Calendar.getInstance();
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
        mCal.set(Calendar.WEEK_OF_YEAR, thisWeek + realPos);
        //Log.d("realPos"," " + realPos);
        //Log.d("mCal"," " + mCal.get(Calendar.YEAR) + "." + mCal.get(Calendar.MONTH));

        setCalendarDate();
        ((ViewPager) container).addView(mRecyclerView);

        return mRecyclerView;
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
        mCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        /*
        for (int i = 0; i < 7; i++) {
            arrData.add(new CalData(mCal.getTime()));
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

         */

        // 이벤트가 없을 경우
        if (Event.upcomingEvents.isEmpty()) {
            for (int i = 0; i < 7; i++) {
                arrData.add(new CalData(mCal.getTime()));
                mCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        else {
            ListIterator<Event> it = Event.upcomingEvents.listIterator();
            ArrayList<Event> eventArrayList = new ArrayList<>();
            Event e = it.next();

            for (int i = 0; i < 7; i++) {
                if (!Time.isCurrentDay(e.getStartTime(), mCal)) {
                    arrData.add(new CalData(mCal.getTime()));
                    mCal.add(Calendar.DAY_OF_MONTH, 1);
                    continue;
                }

                //?
                eventArrayList.add(e);
                while (it.hasNext()) {
                    e = it.next();
                    if (!Time.isCurrentDay(e.getStartTime(), mCal))
                        break;

                    eventArrayList.add(e);
                }

                arrData.add(new CalData(mCal.getTime(), eventArrayList));
                mCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        adapter = new CalendarAdapter2(mView.getContext(), arrData);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.calendar_week_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setId(mCal.get(Calendar.MONTH));
        mRecyclerView.setAdapter(adapter);
    }
}
