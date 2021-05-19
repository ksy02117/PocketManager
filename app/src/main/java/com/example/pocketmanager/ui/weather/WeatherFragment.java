package com.example.pocketmanager.ui.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.pocketmanager.R;
import com.example.pocketmanager.network.AirPollutionReceiver;
import com.example.pocketmanager.network.GeoCodingReceiver;
import com.example.pocketmanager.network.WeatherReceiver;
import com.example.pocketmanager.storage.LocationData;
import com.example.pocketmanager.storage.WeatherData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class WeatherFragment extends Fragment {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView weather_recycler, rain_recycler;
    private MyWeatherAdapter adapter;

    View view;


    public WeatherFragment() {
    }

    public static WeatherFragment newInstance() {
        WeatherFragment weatherFragment = new WeatherFragment();
        return weatherFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tenki, container, false);

        weather_recycler = (RecyclerView) view.findViewById(R.id.weather_by_time_list);
        rain_recycler = (RecyclerView) view.findViewById(R.id.rain_by_time_list);

        weather_recycler.setHasFixedSize(true);
        rain_recycler.setHasFixedSize(true);

        weather_recycler.setLayoutManager(mLayoutManager);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        rain_recycler.setLayoutManager((mLayoutManager));

        getCurrentLocationWeather();


        return view;
    }

    /*
    public void getWeather(float latitude, float longitude) {

        GeoCodingReceiver.getCurrentAddress();
        WeatherData.initCurrentLocationWeatherData();


        WeatherReceiver.getInstance().getWeather(latitude, longitude,
                (result) -> {
                    int i = 0, j = 0;
                    while (i < WeatherData.currentLocationWeatherData.size() && j < result.size()) {
                        WeatherData output = WeatherData.currentLocationWeatherData.get(i);
                        WeatherData input  = result.get(j);
                        if (output.getDt() == input.getDt()) {
                            //main
                            output.setTemp(input.getTemp());
                            output.setFeels_like(input.getFeels_like());
                            output.setHumidity(input.getHumidity());

                            //weather
                            output.setWeather(input.getWeather());
                            output.setDescription(input.getDescription());
                            output.setIcon(input.getIcon());
                            output.setWind_speed(input.getWind_speed());

                            output.setRain_1h(input.getRain_1h());
                            output.setSnow_1h(input.getSnow_1h());
                            i++;
                            j++;
                        }
                        else if (output.getDt() < input.getDt())
                            i++;
                        else
                            j++;
                    }
                    display();
                });

        AirPollutionReceiver.getInstance().getAirPollution(latitude, longitude,
                (result) -> {
                    int i = 0, j = 0;
                    while (i < WeatherData.currentLocationWeatherData.size() && j < result.size()) {
                        WeatherData output = WeatherData.currentLocationWeatherData.get(i);
                        WeatherData input  = result.get(j);
                        if (output.getDt() == input.getDt()) {
                            output.setPm2_5(input.getPm2_5());
                            output.setPm10(input.getPm10());
                            i++;
                            j++;
                        }
                        else if (output.getDt() < input.getDt())
                            i++;
                        else
                            j++;
                    }
                    display();
                });
    }
     */

    public void getCurrentLocationWeather() {

        LocationData.setCurrentLocation();
        WeatherData.initCurrentLocationWeatherData();
        Double latitude = LocationData.getCurrentLocation().getLatitude();
        Double longitude = LocationData.getCurrentLocation().getLongitude();



        WeatherReceiver.getInstance().getWeather(latitude, longitude,
                (result) -> {
                    int i = 0, j = 0;
                    while (i < WeatherData.currentLocationWeatherData.size() && j < result.size()) {
                        WeatherData output = WeatherData.currentLocationWeatherData.get(i);
                        WeatherData input  = result.get(j);
                        if (output.getDt() == input.getDt()) {
                            //main
                            output.setTemp(input.getTemp());
                            output.setFeels_like(input.getFeels_like());
                            output.setHumidity(input.getHumidity());

                            //weather
                            output.setWeather(input.getWeather());
                            output.setDescription(input.getDescription());
                            output.setIcon(input.getIcon());
                            output.setWind_speed(input.getWind_speed());

                            output.setRain_1h(input.getRain_1h());
                            output.setSnow_1h(input.getSnow_1h());
                            i++;
                            j++;
                        }
                        else if (output.getDt() < input.getDt())
                            i++;
                        else
                            j++;
                    }
                    display();
                });

        AirPollutionReceiver.getInstance().getAirPollution(latitude, longitude,
                (result) -> {
                    int i = 0, j = 0;
                    while (i < WeatherData.currentLocationWeatherData.size() && j < result.size()) {
                        WeatherData output = WeatherData.currentLocationWeatherData.get(i);
                        WeatherData input  = result.get(j);
                        if (output.getDt() == input.getDt()) {
                            output.setPm2_5(input.getPm2_5());
                            output.setPm10(input.getPm10());
                            i++;
                            j++;
                        }
                        else if (output.getDt() < input.getDt())
                            i++;
                        else
                            j++;
                    }
                    display();
                });
    }

    public void display() {
        ((TextView)view.findViewById(R.id.max_temperature)).setText("" + LocationData.getCurrentLocation().getLatitude());
        ((TextView)view.findViewById(R.id.min_temperature)).setText("" + LocationData.getCurrentLocation().getLongitude());
        adapter = new MyWeatherAdapter(getActivity(), WeatherData.currentLocationWeatherData);
        weather_recycler.setAdapter(adapter);
        rain_recycler.setAdapter(adapter);
    }

}
