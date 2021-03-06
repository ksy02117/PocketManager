package com.example.pocketmanager.weather;

import com.example.pocketmanager.general.Time;

public class DailyWeatherData {

    private Time time = new Time();
    private float max_temp = 0.0f;
    private float min_temp = 0.0f;
    private float pop = 0.0f;
    private String weather = "";
    private String icon = "";

    public long getDt() { return time.getDt(); }
    public void setDt(long dt) { time.setDt(dt); }

    public long getDateID() { return time.getDateID(); }
    public int getYear() { return time.getYear(); }
    public int getMonth() { return time.getMonth(); }
    public int getDay() { return time.getDay(); }
    public int getHour() { return time.getHour(); }
    public int getMin() { return time.getMin(); }
    public int getSec() { return time.getSec(); }

    public float getMax_temp() { return max_temp; }
    public void setMax_temp(float max_temp) { this.max_temp = max_temp; }
    public float getMin_temp() { return min_temp; }
    public void setMin_temp(float min_temp) { this.min_temp = min_temp; }

    public float getPop() { return pop; }
    public void setPop(float pop) { this.pop = pop; }

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



}
