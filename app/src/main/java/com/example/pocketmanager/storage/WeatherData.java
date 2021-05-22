package com.example.pocketmanager.storage;

import com.example.pocketmanager.network.GeoCodingReceiver;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class WeatherData implements Serializable {

    public static ArrayList<WeatherData> currentLocationWeatherData = new ArrayList<>(48);

    private Time time = new Time();

    private float temp;
    //체감온도
    private float feels_like;
    private float humidity;

    private String weather;
    private String description;
    private String icon;
    private float wind_speed;
    private float rain_1h;
    private float snow_1h;

    //초미세먼지
    private float pm2_5;
    //미세먼지
    private float pm10;

    private LocationData location = new LocationData();




    public static void initCurrentLocationWeatherData() {
        long currentDt = Calendar.getInstance().getTimeInMillis() / 1000;
        long startDt = currentDt / 3600 * 3600;
        long dDt = 3600;
        //LocationData location = GeoCodingReceiver.getCurrentAddress();

        if (currentLocationWeatherData.isEmpty())
            for (int i = 0; i < 48; i++)
                currentLocationWeatherData.add(new WeatherData());

        for (int i = 0; i < 48; i++) {
            currentLocationWeatherData.get(i).setDt(startDt + dDt * i);
//            currentLocationWeatherData.get(i).setLocation(LocationData.getCurrentLocation());
        }

    }


    public long getDt() { return time.getDt(); }
    public void setDt(long dt) { time.setDt(dt); }

    public int getYear() { return time.getYear(); }
    public int getMonth() { return time.getMonth(); }
    public int getDay() { return time.getMin(); }
    public int getHour() { return time.getHour(); }
    public int getMin() { return time.getMin(); }
    public int getSec() { return time.getSec(); }



    public float getTemp() { return temp; }
    public void setTemp(float temp) { this.temp = temp; }
    public float getFeels_like() { return feels_like; }
    public void setFeels_like(float feels_like) { this.feels_like = feels_like; }

    public float getHumidity() { return humidity; }
    public void setHumidity(float humidity) { this.humidity = humidity; }



    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public float getWind_speed() {
        return wind_speed;
    }
    public void setWind_speed(float wind_speed) { this.wind_speed = wind_speed; }

    public float getRain_1h() {
        return rain_1h;
    }
    public void setRain_1h(float rain_1h) {
        this.rain_1h = rain_1h;
    }
    public float getSnow_1h() {
        return snow_1h;
    }
    public void setSnow_1h(float snow_1h) {
        this.snow_1h = snow_1h;
    }



    public float getPm2_5() { return pm2_5; }
    public void setPm2_5(float pm2_5) { this.pm2_5 = pm2_5; }
    public float getPm10() { return pm10; }
    public void setPm10(float pm10) { this.pm10 = pm10; }


/*
    public double getLatitude() { return location.getLatitude(); }
    public double getLongitude() { return location.getLongitude(); }
    public void setLocation(LocationData location) { this.location = location; }
    public void setLocation(double latitude, double longitude) { location.setLocation(latitude, longitude); }
*/
}
