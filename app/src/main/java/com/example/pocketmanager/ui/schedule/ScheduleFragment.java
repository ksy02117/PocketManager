package com.example.pocketmanager.ui.schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.pocketmanager.R;
import java.util.Calendar;

public class ScheduleFragment extends Fragment {
    private int currentYear, currentMonth;
    private Calendar mCalendar;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.schedule, container, false);

        mCalendar = Calendar.getInstance();
        TextView curDate = (TextView) view.findViewById(R.id.calendar_date);

        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(this.getContext());
        ViewPager mPager = (ViewPager) view.findViewById(R.id.calendar_viewpager);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int fromPosition = Integer.MAX_VALUE / 2;

            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fromPosition = position;
            }
            public void onPageSelected(int position) {
                int dPos = fromPosition - position;

                if (dPos < 0)
                    mCalendar.add(Calendar.MONTH, 1);
                else if (dPos > 0)
                    mCalendar.add(Calendar.MONTH, -1);

                curDate.setText(mCalendar.get(Calendar.YEAR) + "년 " + (mCalendar.get(Calendar.MONTH) + 1) + "월");
            }
        });
        mPager.setCurrentItem(Integer.MAX_VALUE / 2);

        return view;
    }


}