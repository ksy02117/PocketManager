package com.example.pocketmanager.storage;

import android.location.Address;

import com.example.pocketmanager.network.GeoCodingReceiver;

import java.util.LinkedList;
import java.util.Locale;

public class LocationData {

    public static LinkedList<LocationData> savedLocation = new LinkedList<>();

    private static LocationData currentLocation = new LocationData();
    private static volatile boolean gpsReady;

    private Address adr = new Address(Locale.US);
    private String description;



    public static void addLocation(LocationData location) {
        savedLocation.add(location);
    }
    public static void remove(LocationData location) {
        savedLocation.remove(location);
    }

    public double getLatitude() { return adr.getLatitude(); }
    public double getLongitude() { return adr.getLongitude(); }
    public String getAddress() {
        String result = "";
        for (int i = 0; i < adr.getMaxAddressLineIndex(); i++)
            result += adr.getAddressLine(i);
        return result;
    }
    public String getName() { return adr.getFeatureName(); }
    public String getDescription() { return description; }

    public void setLocation(double latitude, double longitude) {
        adr = GeoCodingReceiver.getAddressfromCoord(latitude, longitude);
    }
    public void setLocationByName(String name) {
        adr.setFeatureName(name);
        adr = GeoCodingReceiver.getAddressfromName(name);
    }
    public void setAddress(String address) {
        adr = GeoCodingReceiver.getAddressfromName(address);
    }
    public void setDescription(String description) { this.description = description; }


    public static void receiveCurrentLocation() {
        receiveCurrentLocation(null);
    }
    public static void receiveCurrentLocation(Runnable t) {
        gpsReady = false;
        GeoCodingReceiver.getCurrentAddress(
                (result)->{
                    currentLocation.setLocation(result.get(0), result.get(1));
                    gpsReady = true;
                    if (t != null) {
                        synchronized (t) {
                            t.notify();
                        }
                    }
                });
    }
    public static LocationData getCurrentLocation() {
        return currentLocation;
    }
    public static boolean isGpsReady() { return gpsReady; }
}
