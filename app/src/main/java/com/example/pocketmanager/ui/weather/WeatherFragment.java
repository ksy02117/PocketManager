package com.example.pocketmanager.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.pocketmanager.R;
import com.example.pocketmanager.network.AirPollutionReceiver;
import com.example.pocketmanager.network.WeatherReceiver;
import com.example.pocketmanager.storage.WeatherData;

public class WeatherFragment extends Fragment {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private MyWeatherAdapter adapter;
    View view;

    public WeatherFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tenki, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.weather_by_time_list);

        recyclerView.setHasFixedSize(true);
        //adapter = new MyWeatherAdapter(getActivity(), itemList);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        recyclerView.setLayoutManager(mLayoutManager);

        //recyclerView.setAdapter(adapter);

        getWeather(35, 127);


        return view;
    }

    public void getWeather(float latitude, float longitude) {

        WeatherData.initCurrentLocationWeatherData();

        WeatherReceiver.getInstance().getCurrentLocationWeather(latitude, longitude,
                (success) -> {
                    display();
                });

        AirPollutionReceiver.getInstance().getCurrentLocationAirPollution(latitude, longitude,
                (success) -> {
                    display();
                });
    }

    public void display() {
        adapter = new MyWeatherAdapter(getActivity(), WeatherData.currentLocationWeatherData);
        recyclerView.setAdapter(adapter);
    }
}
