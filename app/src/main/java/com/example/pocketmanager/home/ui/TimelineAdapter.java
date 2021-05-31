package com.example.pocketmanager.home.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;

import org.jetbrains.annotations.NotNull;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {
    private int startTime, duration;
    private Context context;

    public TimelineAdapter(Context context, int startTime, int duration) {
        this.context = context;
        this.startTime = startTime;
        this.duration = duration;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.timeline, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TimelineAdapter.ViewHolder holder, int position) {
        holder.timeView.setText((startTime + position) + "시");
    }

    @Override
    public int getItemCount() { return duration + 1; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timeView;

        public ViewHolder(View itemView) {
            super(itemView);
            timeView = itemView.findViewById(R.id.timeline_time);
        }
    }
}
