package com.example.pocketmanager.weather;

import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.weather.receiver.WeatherReceiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WeatherData implements Serializable {

    public static ArrayList<DailyWeatherData> dailyWeatherData = new ArrayList<>(8);
    public static ArrayList<WeatherData> hourlyWeatherData = new ArrayList<>(48);
    public static boolean todayWeatherReady = false;
    public static boolean tomorrowWeatherReady = false;
    public static boolean dailyWeatherReady = false;
    public static boolean airPollutionReady = false;


    private Time time = new Time();

    private float temp = 0.0f;
    //체감온도
    private float feels_like = 0.0f;
    private float humidity = 0.0f;

    private String weather = "";
    private String icon = "";
    private float wind_speed = 0.0f;
    private float pop = 0.0f;
    private float rain = 0.0f;
    private float snow = 0.0f;

    //초미세먼지
    private float pm2_5 = 0.0f;
    //미세먼지
    private float pm10 = 0.0f;


    public WeatherData() {

    }
    public WeatherData(long dt) {
        time.setDt(dt);
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
    public float getMax_temp() {
        for (int i = 0; i < dailyWeatherData.size(); i++) {
            if (time.getDt() / 86400 == dailyWeatherData.get(i).getDt() / 86400)
                return dailyWeatherData.get(i).getMax_temp();
        }
        return 0.0f;
    }
    public float getMin_temp() {
        for (int i = 0; i < dailyWeatherData.size(); i++) {
            if (time.getDt() / 86400 == dailyWeatherData.get(i).getDt() / 86400)
                return dailyWeatherData.get(i).getMin_temp();
        }
        return 0.0f;
    }

    public float getHumidity() { return humidity; }
    public void setHumidity(float humidity) { this.humidity = humidity; }

    public String getWeather() {
        if (weather.equals("Clear"))
            return "맑음";
        if (weather.equals("Clouds"))
            return "구름";
        if (weather.equals("Drizzle"))
            return "이슬비";
        if (weather.equals("Rain"))
            return "비";
        if (weather.equals("Snow"))
            return "눈";
        if (weather.equals("Mist") || weather.equals("Haze") || weather.equals("Fog"))
            return "안개";
        if (weather.equals("Thunderstorm"))
            return "천둥번개";
        return weather;
    }
    public void setWeather(String weather) { this.weather = weather; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public float getWind_speed() { return wind_speed; }
    public void setWind_speed(float wind_speed) { this.wind_speed = wind_speed; }

    public float getPop() { return pop; }
    public void setPop(float pop) { this.pop = pop; }
    public float getRain() { return rain; }
    public void setRain(float rain_1h) { this.rain = rain_1h; }
    public float getSnow() { return snow; }
    public void setSnow(float snow_1h) { this.snow = snow_1h; }

    public float getPm2_5() { return pm2_5; }
    public void setPm2_5(float pm2_5) { this.pm2_5 = pm2_5; }
    public float getPm10() { return pm10; }
    public void setPm10(float pm10) { this.pm10 = pm10; }


    public static WeatherData getCurrentWeather() {
        int index = (int) (Time.getCurrentDt() / 3600 - Time.getCurrentDt() / 3600 / 24 * 24);
        return hourlyWeatherData.get(index);
    }
    public static WeatherData getNextCurrentWeather() {
        return hourlyWeatherData.get(26);
    }
    public static List<WeatherData> getTodayWeather() {
        return hourlyWeatherData.subList(0, 24);
    }
    public static List<WeatherData> getTomorrowWeather() {
        return hourlyWeatherData.subList(24, 48);
    }
    public static List<DailyWeatherData> getDailyWeather() {
        return dailyWeatherData;
    }



    public static void receiveWeatherData() {
        new Thread(WeatherReceiver.getInstance()).start();
    }
    public static boolean isWeatherReady() {
        return WeatherReceiver.isWeatherReady();
    }

}
