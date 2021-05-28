package com.example.pocketmanager.weather.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.LocationData;
import com.example.pocketmanager.weather.WeatherData;

import org.w3c.dom.Text;

public class WeatherFragment extends Fragment {
    private ScrollView mScrollView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView weather_recycler, rain_recycler;
    private MyWeatherAdapter weatherAdapter;
    private MyRainVolumeAdapter rainAdapter;
    int tab;    //0: ~24h | 1: ~48h | 2: ~7d
    View view;

    private static final int TODAY_TAB = 0;
    private static final int TOMORROW_TAB = 1;
    private static final int DAILY_TAB = 2;



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


        display();


        return view;
    }

    public void display() {
        WeatherData curData = null;
        if (tab == TODAY_TAB) {
            weatherAdapter = new MyWeatherAdapter(getActivity(), WeatherData.getTodayWeather());
            rainAdapter = new MyRainVolumeAdapter(getActivity(), WeatherData.getTodayWeather());
            curData = WeatherData.getCurrentWeather();
        }
        else if (tab == TOMORROW_TAB) {
            weatherAdapter = new MyWeatherAdapter(getActivity(), WeatherData.getTomorrowWeather());
            rainAdapter = new MyRainVolumeAdapter(getActivity(), WeatherData.getTomorrowWeather());
            curData = WeatherData.getNextCurrentWeather();
        }
        else {
            weatherAdapter = new MyWeatherAdapter(getActivity(), WeatherData.getTodayWeather());
            rainAdapter = new MyRainVolumeAdapter(getActivity(), WeatherData.getTodayWeather());
            curData = WeatherData.getCurrentWeather();
        }

        weather_recycler.setAdapter(weatherAdapter);
        rain_recycler.setAdapter(rainAdapter);

        TextView locationName = (TextView) view.findViewById(R.id.current_location);
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
        TextView pop = (TextView) view.findViewById(R.id.rain_probability);
        TextView precipitation = (TextView) view.findViewById(R.id.precipitation);
        TextView pm10 = (TextView) view.findViewById(R.id.pm10);
        TextView pm2_5 = (TextView) view.findViewById(R.id.pm2_5);

        int resourceId = getResources().getIdentifier("w_" + curData.getIcon().replace('n', 'd'), "drawable", getContext().getPackageName());
        int pm10Color, pm2_5Color;

        int p1 = (int) curData.getPm10();
        int p2 = (int) curData.getPm2_5();

        locationName.setText(LocationData.getCurrentLocation().getAddress());

        curTemp.setText(String.format("%s℃", Math.round(curData.getTemp())));
        maxTemp.setText(Math.round(curData.getMax_temp()) + "°");
        minTemp.setText(Math.round(curData.getMin_temp()) + "°");
        feelsLike.setText(String.format("체감 %s°", Math.round(curData.getFeels_like())));

        curWeatherImage.setImageResource(resourceId);
        curWeather.setText(curData.getWeather());

        humidity.setText(String.format("%s%%", curData.getHumidity()));
        windSpeed.setText(String.format("%sm/s", curData.getWind_speed()));
        pop.setText((int)(curData.getPop() * 100) + "%");
        precipitation.setText(curData.getRain() + "mm");

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
