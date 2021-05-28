package com.example.pocketmanager.map;

import android.location.Address;

import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.schedule.storage.Event;
import com.example.pocketmanager.schedule.storage.EventDBHelper;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

public class LocationData implements Serializable {

    public static LinkedList<LocationData> locations = new LinkedList<>();
    public static LinkedList<LocationData> favorites = new LinkedList<>();

    private static LocationData currentLocation = new LocationData();
    private static LinkedList<Runnable> pendingTreads = new LinkedList<Runnable>();
    private static boolean gpsReady = false;

    private long id;
    private String name = "";
    private Address adr = new Address(new Locale("ko_kr"));
    private String description;
    private boolean favorite = false;

    private LocationData() {

    }
    private LocationData(long id, String name, Double latitude, Double longitude, boolean favorite, String description) {
        this.id = id;
        this.name = name;
        this.favorite = favorite;
        this.description = description;

        adr = GeoCodingReceiver.getAddressfromCoord(latitude, longitude);
    }

    public static LocationData createLocation(String name, double latitude, double longitude) {
        long id = LocationDBHelper.insert(name, latitude, longitude, false, "");

        LocationData location = new LocationData(id, name, latitude, longitude, false, "");
        locations.add(location);
        return location;
    }

    public static LocationData loadLocationData(long id, String name, Double latitude, Double longitude, boolean favorite, String description) {
        LocationData location = new LocationData(id, name, latitude, longitude, favorite, description);
        locations.add(location);

        return location;
    }





    public static LocationData findLocationByID(long id) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getID() == id)
                return locations.get(i);
        }
        return null;
    }

    public static void addFavorite(LocationData location) {
        location.favorite = true;
        favorites.add(location);
        //update DB
        LocationDBHelper.updateFavorite(location.getID(), true);
    }
    public static void removeFavorite(LocationData location) {
        location.favorite = false;
        favorites.remove(location);
        //update DB
        LocationDBHelper.updateFavorite(location.getID(), false);
    }

    public long getID() { return id; }
    public double getLatitude() { return adr.getLatitude(); }
    public double getLongitude() { return adr.getLongitude(); }
    public String getAddress() {
        StringBuilder builder = new StringBuilder();
        builder.append(adr.getLocality());
        builder.append(" " + adr.getSubLocality());

        return builder.toString();
    }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public void setName(String name) {
        this.name = name;
        //update DB
        LocationDBHelper.updateName(this.getID(), name);
    }
    public void setDescription(String description) {
        this.description = description;
        //update DB
        LocationDBHelper.updateDescription(this.getID(), description);
    }



    public static void addPendingThread(Runnable t) {
        pendingTreads.add(t);
    }
    public static void receiveCurrentLocation() {
        gpsReady = false;
        GeoCodingReceiver.getCurrentAddress(
                (result)->{
                    currentLocation.adr = GeoCodingReceiver.getAddressfromCoord(result.get(0), result.get(1));
                    gpsReady = true;
                    Iterator<Runnable> it = pendingTreads.iterator();
                    while (it.hasNext()) {
                        Runnable t = it.next();
                        synchronized (t) {
                            t.notify();
                        }
                    }
                    pendingTreads.clear();
                });
    }
    public static LocationData getCurrentLocation() {
        return currentLocation;
    }
    public static boolean isGpsReady() { return gpsReady; }
}
