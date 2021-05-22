package com.example.pocketmanager.ui.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.pocketmanager.R;
import com.example.pocketmanager.storage.CalData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class MyPagerAdapter extends PagerAdapter {
    private Calendar mCal;
    private GridView mGridView;
    private CalendarAdapter adapter;
    private ArrayList<CalData> arrData;
    private Context mContext;
    private View mView;
    private int thisMonth, thisYear, thisWeek;

    public MyPagerAdapter(Context context) {
        super();
        mContext = context ;
        mCal = Calendar.getInstance(); // Calendar 객체 생성

        thisWeek = mCal.get(Calendar.WEEK_OF_YEAR);
        thisMonth = mCal.get(Calendar.MONTH);
        thisYear = mCal.get(Calendar.YEAR);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPos = position - 500;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.calendar_grid, container, false);

        mCal.set(Calendar.YEAR, thisYear);
        mCal.set(Calendar.MONTH, thisMonth + realPos);

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
        return 1000;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return (view == object);
    }

    private void setCalendarDate(){
        arrData = new ArrayList<>();

        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int startday = mCal.get(Calendar.DAY_OF_WEEK);

        // move calendar backwards to the beginning of the week
        mCal.add(Calendar.DAY_OF_MONTH, -startday + 1);

        for (int i = 0; i < 42; i++) {
            arrData.add(new CalData(mCal.getTime()));
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        adapter = new CalendarAdapter(mView.getContext(), arrData);
        mGridView = (GridView) mView.findViewById(R.id.calendar_grid);
        mGridView.setId(thisYear);
        mGridView.setAdapter(adapter);
    }
    /*
    private void setCalendarDate(int month){
        arrData = new ArrayList<>();
        mCal.set(Calendar.MONTH, month - 1); // 요일은 +1해야 되기때문에 달력에 요일을 세팅할때에는 -1 해준다.

        // 시작 요일 설정
        Calendar mCalToday = Calendar.getInstance();
        mCalToday.set(thisYear, month - 1, 1);
        int startday = mCalToday.get(Calendar.DAY_OF_WEEK);

        // move calendar backwards to the beginning of the week
        mCalToday.add(Calendar.DAY_OF_MONTH, -startday + 1);

        for (int i = 0; i < 42; i++) {
            arrData.add(new CalData(mCalToday.getTime()));
            mCalToday.add(Calendar.DAY_OF_MONTH, 1);
        }

        adapter = new CalendarAdapter(mView.getContext(), arrData);
        mGridView = (GridView) mView.findViewById(R.id.calendar_grid);
        mGridView.setId(thisYear);
        mGridView.setAdapter(adapter);


        mGridView.setOnItemClickListener((parent, view, position, id) -> {
            int mId = parent.getId();
            Toast.makeText(mContext.getApplicationContext(), "" + mId, Toast.LENGTH_SHORT).show();
        });


    }

     */
}
