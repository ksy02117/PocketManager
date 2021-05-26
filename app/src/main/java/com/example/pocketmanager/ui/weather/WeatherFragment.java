package com.example.pocketmanager.ui.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.network.AirPollutionReceiver;
import com.example.pocketmanager.network.WeatherReceiver;
import com.example.pocketmanager.storage.LocationData;
import com.example.pocketmanager.storage.WeatherData;

public class WeatherFragment extends Fragment {
    private ProgressBar mProgressBar;
    private ScrollView mScrollView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView weather_recycler, rain_recycler;
    private MyWeatherAdapter weatherAdapter;
    private MyRainVolumeAdapter rainAdapter;
    int tab;    //0: ~24h | 1: ~48h | 2: ~7d
    View view;

    public WeatherFragment() {}

    public static WeatherFragment newInstance(int tab) {
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.tab = tab;

        return weatherFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tenki, container, false);

        mScrollView = (ScrollView) view.findViewById(R.id.weather_layout);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        weather_recycler = (RecyclerView) view.findViewById(R.id.weather_by_time_list);
        rain_recycler = (RecyclerView) view.findViewById(R.id.rain_by_time_list);

        weather_recycler.setHasFixedSize(true);
        rain_recycler.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
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
        mScrollView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        LocationData.setCurrentLocation();
        WeatherData.initCurrentLocationWeatherData();

        boolean done = false;
        Double latitude = 0.0;
        Double longitude = 0.0;
        while (!done) {
            try {
                latitude = LocationData.getCurrentLocation().getLatitude();
                longitude = LocationData.getCurrentLocation().getLongitude();
                done = true;
            } catch (Exception e) {
                try {
                    this.wait(1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }

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
                    mProgressBar.setVisibility(View.GONE);
                    mScrollView.setVisibility(View.VISIBLE);
                });

    }

    public void display() {
        weatherAdapter = new MyWeatherAdapter(getActivity(), WeatherData.currentLocationWeatherData);
        rainAdapter = new MyRainVolumeAdapter(getActivity(), WeatherData.currentLocationWeatherData);
        weather_recycler.setAdapter(weatherAdapter);
        rain_recycler.setAdapter(rainAdapter);

        // 기온
        TextView curTemp = (TextView) view.findViewById(R.id.current_temperature);
        TextView maxTemp = (TextView) view.findViewById(R.id.max_temperature);
        TextView minTemp = (TextView) view.findViewById(R.id.min_temperature);
        TextView feelsLike = (TextView) view.findViewById(R.id.feels_like);

        // 날씨
        ImageView curWeatherImage = (ImageView) view.findViewById(R.id.weather_image);
        TextView curWeather = (TextView) view.findViewById(R.id.weather_type);

        // 기타
        TextView humidity = (TextView) view.findViewById(R.id.humidity);
        TextView windSpeed = (TextView) view.findViewById(R.id.wind_speed);
        TextView pm10 = (TextView) view.findViewById(R.id.pm10);
        TextView pm2_5 = (TextView) view.findViewById(R.id.pm2_5);
        WeatherData curData = WeatherData.currentLocationWeatherData.get(0);
        int resourceId = getResources().getIdentifier("w_" + curData.getIcon().replace('n', 'd'), "drawable", getContext().getPackageName());
        int pm10Color, pm2_5Color;

        int p1 = (int) curData.getPm10();
        int p2 = (int) curData.getPm2_5();

        curTemp.setText(String.format("%s℃", curData.getTemp()));
        feelsLike.setText(String.format("체감 %s°", curData.getFeels_like()));

        curWeatherImage.setImageResource(resourceId);
        curWeather.setText(curData.getWeather());

        humidity.setText(String.format("%s%%", curData.getHumidity()));
        windSpeed.setText(String.format("%sm/s", curData.getWind_speed()));

        pm10.setText(String.format("%s", p1));
        pm2_5.setText(String.format("%s", p2));

        // 텍스트 색 결정
        if (p1 <= 30)
            pm10Color = Color.parseColor("#64a0dc"); // blue
        else if (p1 <= 80)
            pm10Color = Color.parseColor("#6edc64"); // green
        else if (p1 <= 150)
            pm10Color = Color.parseColor("#dca064"); // orange
        else
            pm10Color = Color.parseColor("#dc6464"); // red

        if (p2 <= 15)
            pm2_5Color = Color.parseColor("#64a0dc"); // blue
        else if (p2 <= 35)
            pm2_5Color = Color.parseColor("#6edc64"); // green
        else if (p2 <= 75)
            pm2_5Color = Color.parseColor("#dca064"); // orange
        else
            pm2_5Color = Color.parseColor("#dc6464"); // red

        pm10.setTextColor(pm10Color);
        pm2_5.setTextColor(pm2_5Color);
    }
}
