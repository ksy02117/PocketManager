package com.example.pocketmanager.ui.weather;

import android.content.Context;
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

public class MyWeatherAdapter extends RecyclerView.Adapter<MyWeatherAdapter.ViewHolder> {
    private ArrayList<WeatherData> list;
    private Context context;

    public MyWeatherAdapter(Context context, ArrayList<WeatherData> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context 와 parent.getContext()는 같음.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.weather_by_time_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.weatherImage.setImageResource(R.drawable.sun);
        /*
        pm2_5
        pm10;
         */
        holder.timeView.setText(list.get(position).getHour() + "시");
        holder.tempView.setText(list.get(position).getTemp() + "C");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView weatherImage;
        public TextView timeView;
        public TextView tempView;

        public ViewHolder(View itemView) {
            super(itemView);
            weatherImage = itemView.findViewById((R.id.item_icon));
            timeView = itemView.findViewById(R.id.item_time);
            tempView = itemView.findViewById(R.id.item_temp);
        }
    }
}
