package com.example.pocketmanager.ui.schedule;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.storage.CalData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter2 extends RecyclerView.Adapter<CalendarAdapter2.ViewHolder>{
    private ArrayList<CalData> list;
    private Context context;

    public CalendarAdapter2(Context context, ArrayList<CalData> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public CalendarAdapter2.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.calendar_week, parent, false);

        return new CalendarAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CalendarAdapter2.ViewHolder holder, int position) {
        int dayOfWeek = list.get(position).getDate().getDay();
        int dayOfMonth = list.get(position).getDate().getDate();
        int month = list.get(position).getDate().getMonth();
        int year = list.get(position).getDate().getYear();
        Calendar today = Calendar.getInstance();

        if(dayOfWeek == 0)
            holder.dayText.setTextColor(Color.RED);
        else if(dayOfWeek == 6)
            holder.dayText.setTextColor(Color.BLUE);
        else
            holder.dayText.setTextColor(Color.BLACK);

        if (dayOfMonth == today.get(Calendar.DAY_OF_MONTH) &&
                month == today.get(Calendar.MONTH) &&
                year + 1900 == today.get(Calendar.YEAR))
            holder.dayView.setBackgroundColor(Color.parseColor("#ffc9dc"));

        holder.dayText.setText(list.get(position).getDate().getDate() + "");
        holder.dayView.getLayoutParams().width = context.getResources().getDisplayMetrics().widthPixels / 7;
        //holder.dayText.setText("Tl~qkf");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View dayView;
        public TextView dayText;

        public ViewHolder(View itemView) {
            super(itemView);

            dayView = itemView;
            dayText = (TextView) itemView.findViewById(R.id.calendar_week_day);
        }
    }
}
