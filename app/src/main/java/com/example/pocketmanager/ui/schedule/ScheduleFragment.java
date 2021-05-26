package com.example.pocketmanager.ui.schedule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.pocketmanager.R;
import com.example.pocketmanager.storage.Event;
import com.example.pocketmanager.storage.LocationData;
import com.example.pocketmanager.storage.Time;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.ListIterator;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ScheduleFragment extends Fragment implements View.OnClickListener {
    private int currentYear, currentMonth;
    private int currentIndex;
    private Calendar mCalendar;
    private View view;
    private ViewPager mPager;
    private MyPagerAdapter monthAdapter;
    private MyPagerAdapter2 weekAdapter;
    private FloatingActionButton addSchedule;
    private LinearLayout mCalendarLayout;
    private ProgressBar mProgressBar;
    private Point size;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.schedule, container, false);

        mCalendarLayout = (LinearLayout) view.findViewById(R.id.calendar_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.calendar_progress_bar);

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        size = new Point();
        d.getSize(size);

        //  TabLayout 이벤트 처리
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.calendar_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        }) ;

        //  달력
        mCalendar = Calendar.getInstance();
        TextView curDate = (TextView) view.findViewById(R.id.calendar_date);

        // Progress Bar
        mCalendarLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        //  월 달력 어댑터
        monthAdapter = new MyPagerAdapter(this.getContext());
        weekAdapter = new MyPagerAdapter2(this.getContext());
        mPager = (ViewPager) view.findViewById(R.id.calendar_viewpager);
        mPager.setAdapter(monthAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int fromPosition = 100;

            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fromPosition = position;
            }
            public void onPageSelected(int position) {
                int rPos = position - 100;
                currentIndex = position;

                mCalendar = Calendar.getInstance();
                mCalendar.add(Calendar.MONTH, rPos);

                curDate.setText(mCalendar.get(Calendar.YEAR) + "년 " + (mCalendar.get(Calendar.MONTH) + 1) + "월");
            }
        });

        mPager.setCurrentItem(100);
        addSchedule = (FloatingActionButton) view.findViewById(R.id.add_schedule);
        addSchedule.setOnClickListener(this);

        // Progress Bar
        mCalendarLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        return view;
    }

    private void changeView(int index) {
        mPager.removeAllViews();
        //mPager.getAdapter().notifyDataSetChanged();
        mPager.setAdapter(null);

        switch (index) {
            case 0 :
                mPager.setAdapter(monthAdapter);
                break ;
            case 1 :
                mPager.setAdapter(weekAdapter);
                break ;
        }
        mPager.setCurrentItem(100);
    }

    @Override
    public void onClick(View v) {
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this.getContext(), addScheduleActivity.class);
        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode==1 && data != null){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                String eventName = data.getStringExtra("event_name");
                String eventDesc = data.getStringExtra("event_description");
                Time startTime = new Time();
                Time endTime = new Time();
                long startDt = data.getLongExtra("start_time", 0);
                long endDt = data.getLongExtra("end_time", 0);
                startTime.setDt(startDt / 1000);
                endTime.setDt(endDt / 1000);
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);

                LocationData ld = new LocationData();
                ld.setLocation(latitude, longitude);

                Event.addEvent(eventName, startTime, endTime, ld, eventDesc, false);
                for (Event e : Event.upcomingEvents)
                    Log.d("이벤트", "" + e.getEventName());

                monthAdapter = new MyPagerAdapter(this.getContext());
                weekAdapter = new MyPagerAdapter2(this.getContext());
                mPager.setAdapter(monthAdapter);
                mPager.setCurrentItem(currentIndex);
            }
        }
    }
}