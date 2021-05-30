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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.CalData;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.schedule.storage.Event;
import com.example.pocketmanager.schedule.storage.SubEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DisplayScheduleAdapter extends RecyclerView.Adapter<DisplayScheduleAdapter.ViewHolder>{
    private ArrayList<CalData> list;
    private Context context;
    private RelativeLayout ll;
    private float scale;
    View view;

    public DisplayScheduleAdapter(Context context, ArrayList<CalData> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public DisplayScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context)
                .inflate(R.layout.calendar_week_display_schedule, parent, false);

        scale = this.context.getResources().getDisplayMetrics().density;

        return new DisplayScheduleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DisplayScheduleAdapter.ViewHolder holder, int position) {
        holder.dayView.getLayoutParams().width = (context.getResources().getDisplayMetrics().widthPixels - getPixel(40)) / 3;
        LinkedList<Event> eventArrayList = list.get(position).getEvents();

        // 이벤트 비어있으면
        if (Event.events.isEmpty() || eventArrayList == null)
            return;

        for (Event e : eventArrayList)
            addSchedule(e);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View dayView;

        public ViewHolder(View itemView) {
            super(itemView);

            dayView = itemView;
        }
    }

    private void addSchedule(Event e) {
        TextView test;
        ll = (RelativeLayout) view.findViewById(R.id.calendar_week_display_schedule);
        LinearLayout.LayoutParams params;


        Time startTime = e.getStartTime();
        int untilStart = getPixel((int) startTime.getHour() * 60 + startTime.getMin());
        int duration = getPixel((int) (e.getEndTime().getDt() - e.getStartTime().getDt()) / 60);
        int bgColor = this.context.getResources().getColor(R.color.parentEventRed);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, duration);
        params.setMargins(0, untilStart, 0, 0);

        test = new TextView(context);
        test.setLayoutParams(params);
        test.setText(e.getEventName());
        test.setBackgroundColor(bgColor);
        test.setTextColor(Color.WHITE);
        test.setGravity(Gravity.LEFT);
        test.setMaxLines(1);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , EventDetailsActivity.class);
                intent.putExtra("event", e);
                ((Activity) context).startActivityForResult(intent, 1);
            }
        });

        ll.addView(test);
        for (Map.Entry<Long, LinkedList<SubEvent>> longLinkedListEntry : e.getSubEvents().entrySet()) {
            List<SubEvent> list = longLinkedListEntry.getValue();
            for (SubEvent event : list) {
                addSubEvent(event);
            }
        }
    }

    private void addSubEvent(SubEvent e) {
        TextView test;
        RelativeLayout.LayoutParams params;
        int startHour, endHour;
        int childColor;
        int leftMargin;
        int duration, untilStart;

        startHour = e.getStartTime().getHour();
        endHour = e.getEndTime().getHour();
        if (e.getEndTime().getMin() != 0)
            endHour += 1;

        duration = getPixel((int) (e.getEndTime().getDt() - e.getStartTime().getDt()) / 60);
        untilStart = getPixel((int) e.getStartTime().getHour() * 60 + e.getStartTime().getMin());
        leftMargin = getPixel(10);
        childColor =  view.getResources().getColor(R.color.childEventGreen);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, duration);
        params.setMargins(leftMargin, untilStart, 0, 0);

        test = new TextView(view.getContext());
        test.setLayoutParams(params);
        test.setText(e.getEventName());
        test.setTextSize(16);
        test.setTextColor(Color.WHITE);
        test.setMaxLines(1);
        test.setGravity(Gravity.RIGHT);
        test.setBackgroundColor(childColor);

        ll.addView(test);
    }

    private int getPixel(int dp) {
        return (int) (dp * scale + 0.5f);
    }
}
