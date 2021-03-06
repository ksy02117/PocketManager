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
import com.example.pocketmanager.weather.DailyWeatherData;
import com.example.pocketmanager.weather.WeatherData;

import org.w3c.dom.Text;

import java.util.List;


public class MyDailyAdapter extends RecyclerView.Adapter<MyDailyAdapter.ViewHolder> {
    private List<DailyWeatherData> list;
    private Context context;

    public MyDailyAdapter(Context context, List<DailyWeatherData> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context 와 parent.getContext()는 같음.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.daily_weather_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DailyWeatherData w = list.get(position);

        holder.weatherImage.setImageDrawable(getDrawableIcon(w.getIcon()));

        String currentDate = w.getMonth() + "월 " + w.getDay() + "일";
        float pop = w.getPop();
        holder.currentDay.setText(currentDate);
        holder.currentWeather.setText(w.getWeather());
        holder.maxTemp.setText(Math.round(w.getMax_temp()) + "℃");
        holder.minTemp.setText(Math.round(w.getMin_temp()) + "℃");
        if (pop == 0)
            holder.currentPop.setVisibility(View.GONE);
        else
            holder.currentPop.setText((int)(w.getPop() * 100) + "%");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView weatherImage;
        public TextView currentDay;
        public TextView currentWeather;
        public TextView currentPop;
        public TextView maxTemp, minTemp;

        public ViewHolder(View itemView) {
            super(itemView);
            weatherImage = itemView.findViewById((R.id.daily_icon));
            currentDay = itemView.findViewById(R.id.daily_day);
            currentWeather = itemView.findViewById(R.id.daily_weather);
            currentPop = itemView.findViewById(R.id.daily_pop);
            maxTemp = itemView.findViewById(R.id.daily_max);
            minTemp = itemView.findViewById(R.id.daily_min);
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
