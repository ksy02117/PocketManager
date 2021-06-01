package com.example.pocketmanager.schedule.storage;

import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.weather.WeatherData;
import com.example.pocketmanager.weather.receiver.EventWeatherReceiver;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class AbstractEvent implements Serializable {
    protected long id;
    protected Time startTime = new Time();
    protected Time endTime = new Time();
    protected String eventName;
    protected String description;
    protected boolean outdoor;
    protected int priority;

    LocationData location;
    ArrayList<WeatherData> eventWeather = new ArrayList<>();
    public boolean isWeatherReady = false;


    //const
    public static final int PRIORITY_HIGH = 0;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_LOW = 2;
    public static final int PRIORITY_TRANS = 3;



    protected AbstractEvent(long id, String name, Time startTime, Time endTime, LocationData location, boolean outdoor, String description, int priority) {
        this.id = id;
        this.eventName = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
        this.outdoor = outdoor;
        this.priority = priority;

        Thread t = new Thread(()->{
            //make placeholder for weather data
            eventWeather = new ArrayList<WeatherData>();
            long startTimeID = startTime.getDt() / 3600 * 3600;
            for (long dt = startTimeID; dt <= endTime.getDt(); dt += 3600) {
                eventWeather.add(new WeatherData(dt));
            }

            Time time = new Time();
            if (endTime.getDt() < new Time(time.getYear(), time.getMonth(), time.getDay(), 0, 0, 0).getDt())
                return;
            if (startTime.getDt() > new Time(time.getYear(), time.getMonth(), time.getDay(), 23, 59, 59).getDt())
                return;
            if (location == null) {
                return;
            }

            //receive weather data
            EventWeatherReceiver.getInstance().getEventWeather(location.getLatitude(), location.getLongitude(), (result)-> {
                int i = 0, j = 0;
                while (i < eventWeather.size() && j < result.size()) {
                    WeatherData output = eventWeather.get(i);
                    WeatherData input  = result.get(j);

                    if (output.getDt() == input.getDt()) {
                        //main
                        output.setTemp(input.getTemp());
                        output.setFeels_like(input.getFeels_like());
                        output.setHumidity(input.getHumidity());

                        //weather
                        output.setWeather(input.getWeather());
                        output.setIcon(input.getIcon());
                        output.setWind_speed(input.getWind_speed());

                        output.setPop(0);
                        output.setRain(input.getRain());
                        output.setSnow(input.getSnow());
                        i++;
                        j++;
                    }
                    else if (output.getDt() < input.getDt())
                        i++;
                    else
                        j++;
                }
                isWeatherReady = true;
            });
        });

        if (outdoor && location != null)
            t.start();
        else
            isWeatherReady = true;
    }



    public long getID() { return id; }
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }
    public String getEventName() { return eventName; }
    public String getDescription() { return description; }
    public int getPriority() { return priority; }
    public LocationData getLocation() { return location; }
    public boolean isOutdoor() { return outdoor; }
    public ArrayList<WeatherData> getEventWeather() { return eventWeather; }


    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public void setLocation(LocationData location) {
        this.location = location;
    }
    public void setOutdoor(boolean outdoor) {
        this.outdoor = outdoor;
    }
}
