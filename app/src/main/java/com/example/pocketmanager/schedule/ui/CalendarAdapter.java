package com.example.pocketmanager.schedule.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.CalData;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.schedule.storage.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class CalendarAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<CalData> arrData;
    private LayoutInflater inflater;
    private LinearLayout l;
    private float scale;
    private Time todayStartTime;

    public CalendarAdapter(Context c, ArrayList<CalData> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() { return arrData.size(); }

    public Object getItem(int position) { return arrData.get(position); }

    public long getItemId(int position) { return position; }

    public ArrayList<CalData> getArrData() { return arrData; }
    public void setArrData(ArrayList<CalData> arrData) { this.arrData = arrData; }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.calendar_item, parent, false);

        scale = parent.getResources().getDisplayMetrics().density;

        TextView ViewText = (TextView)convertView.findViewById(R.id.ViewText);
        ImageView ViewImage = (ImageView) convertView.findViewById(R.id.highlight);
        Date tmpDate = arrData.get(position).getDate();
        LinkedList<Event> eventArrayList = arrData.get(position).getEvents();

        Calendar tmpCal = Calendar.getInstance();
        int thisMonth = arrData.get(7).getDate().getMonth();
        int thisYear = tmpCal.get(Calendar.YEAR);
        int today = tmpCal.get(Calendar.DAY_OF_MONTH);

        tmpCal.set(Calendar.DAY_OF_MONTH, tmpDate.getDate());
        int dayOfWeek = tmpDate.getDay();

        ViewText.setText(tmpDate.getDate() + "");

        int baseGray = convertView.getResources().getColor(R.color.baseGray);
        int baseColor = convertView.getResources().getColor(R.color.baseTextColor);
        int baseBlue = convertView.getResources().getColor(R.color.baseCalendarBlue);
        int baseRed = convertView.getResources().getColor(R.color.baseCalendarRed);

        if (tmpDate.getMonth() != thisMonth) {
            ViewText.setTextColor(baseGray);

            return convertView;
        }

        if(dayOfWeek == 0)
            ViewText.setTextColor(baseRed);
        else if(dayOfWeek == 6)
            ViewText.setTextColor(baseBlue);
        else
            ViewText.setTextColor(baseColor);

        if (tmpDate.getDate() == today && tmpDate.getMonth() == tmpCal.get(Calendar.MONTH) &&
                tmpDate.getYear() + 1900 == thisYear) {
            ViewText.setTextColor(Color.BLACK);
            ViewImage.setVisibility(View.VISIBLE);
        }

        if (Event.events.isEmpty() || eventArrayList == null)
            return convertView;

        l = (LinearLayout) convertView.findViewById(R.id.rlItemViewCalendar);

        for (Event e : eventArrayList)
            addSchedule(e);

        return convertView;
    }

    private void addSchedule(Event e) {
        TextView test;

        test = new TextView(context);
        test.setText(e.getEventName());
        test.setGravity(Gravity.CENTER);
        test.setTextSize(10);
        test.setMaxLines(1);
        test.setPadding(0, 0, 0, 0);

        l.addView(test);
    }

    private int getPixel(int dp) {
        return (int) (dp * scale + 0.5f);
    }
}