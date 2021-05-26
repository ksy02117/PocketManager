package com.example.pocketmanager.ui.weather;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.storage.WeatherData;

import java.util.ArrayList;

public class MyRainVolumeAdapter extends RecyclerView.Adapter<MyRainVolumeAdapter.ViewHolder> {
    private ArrayList<WeatherData> list;
    private Context context;

    public MyRainVolumeAdapter(Context context, ArrayList<WeatherData> list) {
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
        float rain = list.get(position).getRain_1h();

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
