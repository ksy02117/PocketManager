package com.example.pocketmanager.schedule.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.CalData;
import com.example.pocketmanager.schedule.storage.Event;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class CalendarAdapter2 extends RecyclerView.Adapter<CalendarAdapter2.ViewHolder>{
    private ArrayList<CalData> list;
    private Context context;
    private float scale;
    View view;

    public CalendarAdapter2(Context context, ArrayList<CalData> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public CalendarAdapter2.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context)
                .inflate(R.layout.calendar_week, parent, false);

        scale = this.context.getResources().getDisplayMetrics().density;

        return new CalendarAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CalendarAdapter2.ViewHolder holder, int position) {
        int dayOfWeek = list.get(position).getDate().getDay();
        int dayOfMonth = list.get(position).getDate().getDate();
        int month = list.get(position).getDate().getMonth();
        int year = list.get(position).getDate().getYear();
        Calendar today = Calendar.getInstance();

        int baseColor = view.getResources().getColor(R.color.baseTextColor);
        int baseBlue = view.getResources().getColor(R.color.baseCalendarBlue);
        int baseRed = view.getResources().getColor(R.color.baseCalendarRed);

        if(dayOfWeek == 0)
            holder.dayText.setTextColor(baseRed);
        else if(dayOfWeek == 6)
            holder.dayText.setTextColor(baseBlue);
        else
            holder.dayText.setTextColor(baseColor);

        if (dayOfMonth == today.get(Calendar.DAY_OF_MONTH) &&
                month == today.get(Calendar.MONTH) &&
                year + 1900 == today.get(Calendar.YEAR)) {
            holder.dayOfMonthText.setTextColor(Color.BLACK);
            holder.dayText.setTextColor(Color.BLACK);
            holder.dayView.setBackgroundColor(Color.parseColor("#ffc9dc"));
        }

        holder.dayText.setText(list.get(position).getDate().getDate() + "");
        holder.dayOfMonthText.setText(convertDayOfMonth(dayOfWeek));
        holder.dayView.getLayoutParams().width = (context.getResources().getDisplayMetrics().widthPixels - getPixel(40)) / 3;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View dayView;
        public TextView dayOfMonthText;
        public TextView dayText;

        public ViewHolder(View itemView) {
            super(itemView);

            dayView = itemView;
            dayOfMonthText = (TextView) itemView.findViewById(R.id.calendar_day_of_month);
            dayText = (TextView) itemView.findViewById(R.id.calendar_week_day);
        }
    }

    private String convertDayOfMonth(int dayOfWeek) {
        switch(dayOfWeek) {
            case 0:
                return "일";
            case 1:
                return "월";
            case 2:
                return "화";
            case 3:
                return "수";
            case 4:
                return "목";
            case 5:
                return "금";
            default:
                return "토";
        }
    }

    private int getPixel(int dp) {
        return (int) (dp * scale + 0.5f);
    }
}
