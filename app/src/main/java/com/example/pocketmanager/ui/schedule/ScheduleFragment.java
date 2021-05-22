package com.example.pocketmanager.ui.schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.pocketmanager.R;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.Calendar;

public class ScheduleFragment extends Fragment {
    private int currentYear, currentMonth;
    private int currentIndex;
    private Calendar mCalendar;
    private View view;
    private ViewPager mPager;
    private MyPagerAdapter monthAdapter;
    private MyPagerAdapter2 weekAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.schedule, container, false);

        //  TabLayout 이벤트 처리
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.calendar_tab) ;
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

        //  월 달력 어댑터
        monthAdapter = new MyPagerAdapter(this.getContext());
        weekAdapter = new MyPagerAdapter2(this.getContext());
        mPager = (ViewPager) view.findViewById(R.id.calendar_viewpager);
        mPager.setAdapter(monthAdapter);
        /*
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int fromPosition = Integer.MAX_VALUE / 2;

            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fromPosition = position;
            }
            public void onPageSelected(int position) {
                int dPos = fromPosition - position;
                currentIndex = position;

                if (dPos < 0)
                    mCalendar.add(Calendar.MONTH, 1);
                else if (dPos > 0)
                    mCalendar.add(Calendar.MONTH, -1);

                curDate.setText(mCalendar.get(Calendar.YEAR) + "년 " + (mCalendar.get(Calendar.MONTH) + 1) + "월");
            }
        });

         */
        mPager.setCurrentItem(500);

        return view;
    }

    private void changeView(int index) {
        mPager.removeAllViews();
        mPager.getAdapter().notifyDataSetChanged();
        mPager.setAdapter(null);

        switch (index) {
            case 0 :
                mPager.setAdapter(monthAdapter);
                break ;
            case 1 :
                mPager.setAdapter(weekAdapter);
                break ;
        }
        mPager.setCurrentItem(500);
        //Toast.makeText(this.getContext(), "" + mPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
    }

}