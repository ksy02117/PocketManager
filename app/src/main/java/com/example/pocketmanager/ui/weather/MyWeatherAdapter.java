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

import java.util.ArrayList;

public class MyWeatherAdapter extends RecyclerView.Adapter<MyWeatherAdapter.ViewHolder> {
    private ArrayList<WeatherItemData> list;
    private Context context;

    public MyWeatherAdapter(Context context, ArrayList<WeatherItemData> list) {
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
        holder.timeview.setText(list.get(position).time + "시");
        holder.tempview.setText(list.get(position).temp);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView weatherImage;
        public TextView timeview;
        public TextView tempview;

        public ViewHolder(View itemView) {
            super(itemView);
            weatherImage = itemView.findViewById((R.id.item_icon));
            timeview = itemView.findViewById(R.id.item_time);
            tempview = itemView.findViewById(R.id.item_temp);
        }
    }
}
