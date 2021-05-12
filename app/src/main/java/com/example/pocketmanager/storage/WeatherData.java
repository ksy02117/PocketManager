package com.example.pocketmanager.storage;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

public class WeatherData implements Serializable {
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
    private float wind_speed;
    private float rain_3h;
    private float snow_3h;

    //초미세먼지
    private float pm2_5;
    //미세먼지
    private float pm10;

    private String city_name;
    private String country;
    private float latitude;
    private float longitude;

    public long getDt() { return dt; }
    public void setDt(long dt) {
        this.dt = dt;
        setTime(dt);
    }
    private void setTime(long dt) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
        cal.setTimeInMillis(dt * 1000L);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
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
    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }



    public String getWeather() {
        return weather;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getWind_speed() {
        return wind_speed;
    }
    public void setWind_speed(float wind_speed) {
        this.wind_speed = wind_speed;
    }

    public float getRain_3h() {
        return rain_3h;
    }
    public void setRain_3h(float rain_3h) {
        this.rain_3h = rain_3h;
    }

    public float getSnow_3h() {
        return snow_3h;
    }
    public void setSnow_3h(float snow_3h) {
        this.snow_3h = snow_3h;
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
