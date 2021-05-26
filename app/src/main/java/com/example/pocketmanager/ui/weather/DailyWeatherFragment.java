package com.example.pocketmanager.ui.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.storage.WeatherData;

public class DailyWeatherFragment extends Fragment {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView dailyRecycler;
    private MyDailyAdapter dailyAdapter;
    View view;

    public DailyWeatherFragment() {}

    public static DailyWeatherFragment newInstance() {
        DailyWeatherFragment weatherFragment = new DailyWeatherFragment();

        return weatherFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tenki_daily, container, false);

        dailyRecycler = (RecyclerView) view.findViewById(R.id.daily_weather_view);

        dailyRecycler.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // 기본값이 VERTICAL
        dailyRecycler.setLayoutManager(mLayoutManager);

        dailyAdapter = new MyDailyAdapter(getActivity(), WeatherData.getDailyWeather());
        dailyRecycler.setAdapter(dailyAdapter);

        return view;
    }
}
