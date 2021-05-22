package com.example.pocketmanager.storage;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class WeatherData implements Serializable {

    public static ArrayList<WeatherData> currentLocationWeatherData = new ArrayList<>(48);
    public static ArrayList<WeatherData> eventBasedWeatherData = new ArrayList<>(20);

    //test1

    private long dt;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private int sec;

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

    private String city_name;
    private String country;
    private float latitude;
    private float longitude;




    public static void initCurrentLocationWeatherData() {
        long currentDt = Calendar.getInstance().getTimeInMillis() / 1000;
        long startDt = currentDt / 3600 * 3600;
        long dDt = 3600;

        if (currentLocationWeatherData.isEmpty())
            for (int i = 0; i < 48; i++)
                currentLocationWeatherData.add(new WeatherData());

        for (int i = 0; i < 48; i++)
            currentLocationWeatherData.get(i).setDt(startDt + dDt*i);
    }


    public long getDt() { return dt; }
    public void setDt(long dt) {
        this.dt = dt;
        setTime(dt);
    }
    private void setTime(long dt) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
        cal.setTimeInMillis(dt * 1000L);

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);
        sec = cal.get(Calendar.SECOND);
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
    public int getHour() { return hour; }
    public int getMin() { return min; }
    public int getSec() { return sec; }



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



    public String getCity_name() {
        return city_name;
    }
    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public float getLatitude() { return latitude; }
    public void setLatitude(float latitude) { this.latitude = latitude; }

    public float getLongitude() { return longitude; }
    public void setLongitude(float longitude) { this.longitude = longitude; }

}
