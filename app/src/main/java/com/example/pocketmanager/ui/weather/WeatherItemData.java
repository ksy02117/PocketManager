package com.example.pocketmanager.ui.weather;

import java.util.ArrayList;

public class WeatherItemData {
    public String weather_type;
    public String time;
    public String temp;

    public WeatherItemData(String weather_type, String time, String temp) {
        this.weather_type = weather_type;
        this.time = time;
        this.temp = temp;
    }

    public static ArrayList<WeatherItemData> createContactsList(int numContacts) {
        ArrayList<WeatherItemData> contacts = new ArrayList<WeatherItemData>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new WeatherItemData("sunny", "10", "50°"));
        }

        return contacts;
    }
}
