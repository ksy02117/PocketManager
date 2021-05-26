package com.example.pocketmanager.weather.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.weather.WeatherData;

import java.util.List;

public class MyRainVolumeAdapter extends RecyclerView.Adapter<MyRainVolumeAdapter.ViewHolder> {
    private List<WeatherData> list;
    private Context context;

    public MyRainVolumeAdapter(Context context, List<WeatherData> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context 와 parent.getContext()는 같음.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.rain_by_time_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        float rain = list.get(position).getRain();

        holder.rainVolume.setScaleY(rain / 15);
        holder.timeView.setText(list.get(position).getHour() + "시");
        holder.rainView.setText(rain + "mm");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View rainVolume;
        public TextView timeView;
        public TextView rainView;

        public ViewHolder(View itemView) {
            super(itemView);
            rainVolume = itemView.findViewById((R.id.item_image));
            timeView = itemView.findViewById(R.id.item_time);
            rainView = itemView.findViewById(R.id.item_rain);
        }
    }
}
