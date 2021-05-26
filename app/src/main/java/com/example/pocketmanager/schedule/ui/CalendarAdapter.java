package com.example.pocketmanager.schedule.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.CalData;
import com.example.pocketmanager.schedule.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<CalData> arrData;
    private LayoutInflater inflater;
    private int dpAsPixels;

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

        float scale = parent.getResources().getDisplayMetrics().density;
        dpAsPixels = (int) (10 * scale + 0.5f);

        TextView ViewText = (TextView)convertView.findViewById(R.id.ViewText);
        ImageView ViewImage = (ImageView) convertView.findViewById(R.id.highlight);
        Date tmpDate = arrData.get(position).getDate();
        ArrayList<Event> eventArrayList = arrData.get(position).getEvents();

        Calendar tmpCal = Calendar.getInstance();
        int thisMonth = arrData.get(7).getDate().getMonth();
        int thisYear = tmpCal.get(Calendar.YEAR);
        int today = tmpCal.get(Calendar.DAY_OF_MONTH);

        tmpCal.set(Calendar.DAY_OF_MONTH, tmpDate.getDate());
        int dayOfWeek = tmpDate.getDay();

        ViewText.setText(tmpDate.getDate() + "");

        if (tmpDate.getMonth() != thisMonth) {
            ViewText.setTextColor(Color.LTGRAY);

            return convertView;
        }

        if(dayOfWeek == 0)
            ViewText.setTextColor(Color.RED);
        else if(dayOfWeek == 6)
            ViewText.setTextColor(Color.BLUE);
        else
            ViewText.setTextColor(Color.BLACK);

        if (tmpDate.getDate() == today && tmpDate.getMonth() == tmpCal.get(Calendar.MONTH) &&
                tmpDate.getYear() + 1900 == thisYear)
            ViewImage.setVisibility(View.VISIBLE);

        // 이벤트 비어있으면
        if (Event.upcomingEvents.isEmpty() || eventArrayList == null)
            return convertView;

        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.rlItemViewCalendar);


        for (Event e : eventArrayList) {
            Log.d("task start!", "-------------");
            Log.d("" + e.getEventName(), "" + e.getStartTime());
            addSchedule(ll, e);
        }

        return convertView;
    }

    private void addSchedule(LinearLayout ll, Event e) {
        TextView test;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        test = new TextView(context);
        test.setLayoutParams(params);
        test.setText(e.getEventName());
        test.setTextSize(10);
        test.setPadding(dpAsPixels, 0, 0, 0);

        ll.addView(test);
    }
}