package com.example.pocketmanager.schedule.ui;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.pocketmanager.R;
import com.example.pocketmanager.home.ui.HomeFragment;
import com.example.pocketmanager.schedule.storage.Event;
import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.general.Time;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.concurrent.LinkedBlockingDeque;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ScheduleFragment extends Fragment implements View.OnClickListener {
    private WindowManager wm;
    private int currentIndex;
    private Calendar mCalendar;
    private View view;
    private ViewPager mPager;
    private MyPagerAdapter monthAdapter;
    private MyPagerAdapter2 weekAdapter;
    private FloatingActionButton fabMenu, fabEveryTime, fabAddSchedule;
    private LinearLayout linEveryTime, linAddSchedule;
    private TextView textAddSchedule, textEveryTime;
    private boolean isFABOpen;
    private RelativeLayout mScheduleLayout;
    private LinearLayout mCalendarLayout, mDayOfWeekLayout;
    private ProgressBar mProgressBar;
    private TabLayout tabLayout;
    private Point size;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.schedule, container, false);

        fabMenu = (FloatingActionButton) view.findViewById(R.id.add_fab);
        fabEveryTime = (FloatingActionButton) view.findViewById(R.id.add_from_everytime);
        fabAddSchedule = (FloatingActionButton) view.findViewById(R.id.add_schedule);
        linAddSchedule = (LinearLayout) view.findViewById(R.id.add_schedule_view);
        linEveryTime = (LinearLayout) view.findViewById(R.id.add_from_everytime_view);
        textAddSchedule = (TextView) view.findViewById(R.id.add_schedule_text);
        textEveryTime = (TextView) view.findViewById(R.id.add_from_everytime_text);

        fabEveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                //데이터 담아서 팝업(액티비티) 호출
                Intent intent = new Intent(getContext(), addEveryTimeActivity.class);
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);
            }
        });

        fabAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                //데이터 담아서 팝업(액티비티) 호출
                Intent intent = new Intent(getContext(), addScheduleActivity.class);
                intent.putExtra("type", "a");
                startActivityForResult(intent, 1);
            }
        });

        mScheduleLayout = (RelativeLayout) view.findViewById(R.id.schedule_layout);
        mCalendarLayout = (LinearLayout) view.findViewById(R.id.calendar_view);
        mDayOfWeekLayout = (LinearLayout) view.findViewById(R.id.schedule_day_of_week);
        mProgressBar = (ProgressBar) view.findViewById(R.id.calendar_progress_bar);

        wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        size = new Point();
        d.getSize(size);

        //  TabLayout 이벤트 처리
        tabLayout = (TabLayout) view.findViewById(R.id.calendar_tab);
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
                if (tabLayout.getSelectedTabPosition() == 0)
                    mCalendar.add(Calendar.MONTH, rPos);
                else
                    mCalendar.add(Calendar.DAY_OF_YEAR, rPos * 3);

                curDate.setText(mCalendar.get(Calendar.YEAR) + "년 " + (mCalendar.get(Calendar.MONTH) + 1) + "월");
            }
        });

        mPager.setCurrentItem(100);
        fabMenu.setOnClickListener(this);

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
                mDayOfWeekLayout.setVisibility(View.VISIBLE);
                break ;
            case 1 :
                mPager.setAdapter(weekAdapter);
                mDayOfWeekLayout.setVisibility(View.GONE);
                break ;
        }
        mPager.setCurrentItem(100);
    }

    @Override
    public void onClick(View v) {
        if (!isFABOpen)
            showFABMenu();
        else
            closeFABMenu();
    }

    private void showFABMenu(){
        isFABOpen=true;
        mPager.setAlpha(0.5f);
        linAddSchedule.animate().translationY(-getResources().getDimension(R.dimen.standard_75));
        linEveryTime.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
        textAddSchedule.setVisibility(View.VISIBLE);
        textEveryTime.setVisibility(View.VISIBLE);
    }

    private void closeFABMenu(){
        isFABOpen=false;
        mPager.setAlpha(1.0f);
        linAddSchedule.animate().translationY(0);
        linEveryTime.animate().translationY(0);
        textAddSchedule.setVisibility(View.INVISIBLE);
        textEveryTime.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode==1){
            //if (resultCode == RESULT_OK) {

                monthAdapter = new MyPagerAdapter(this.getContext());
                weekAdapter = new MyPagerAdapter2(this.getContext());
                if (tabLayout.getSelectedTabPosition() == 0)
                    mPager.setAdapter(monthAdapter);
                else
                    mPager.setAdapter(weekAdapter);
                mPager.setCurrentItem(currentIndex);
            //}
        }
    }
}