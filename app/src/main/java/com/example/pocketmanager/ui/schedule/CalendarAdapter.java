package com.example.pocketmanager.ui.schedule;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.storage.CalData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<CalData> arrData;
    private LayoutInflater inflater;

    public CalendarAdapter(Context c, ArrayList<CalData> arr) {
        this.context = c;
        this.arrData = arr;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() { return arrData.size(); }

    public Object getItem(int position) { return arrData.get(position); }

    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.calendar_item, parent, false);

        TextView ViewText = (TextView)convertView.findViewById(R.id.ViewText);
        ImageView ViewImage = (ImageView) convertView.findViewById(R.id.highlight);
        Date tmpDate = arrData.get(position).getDate();

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

        return convertView;
    }
}