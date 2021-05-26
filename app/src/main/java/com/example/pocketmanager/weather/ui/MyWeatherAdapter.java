package com.example.pocketmanager.weather.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.weather.WeatherData;

import java.util.List;

public class MyWeatherAdapter extends RecyclerView.Adapter<MyWeatherAdapter.ViewHolder> {
    private List<WeatherData> list;
    private Context context;

    public MyWeatherAdapter(Context context, List<WeatherData> list) {
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
        holder.weatherImage.setImageDrawable(getDrawableIcon(list.get(position).getIcon()));
        /*
        pm2_5
        pm10;
         */
        holder.timeView.setText(list.get(position).getHour() + "시");
        holder.tempView.setText(Math.round(list.get(position).getTemp()) + "°");

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

    public Drawable getDrawableIcon(String icon) {
        Resources resources = context.getResources();
        int resourceId;
        String mIcon = icon.replace('n', 'd');

        resourceId = resources.getIdentifier("w_" + mIcon, "drawable", context.getPackageName());

        return resources.getDrawable(resourceId);
    }
}
