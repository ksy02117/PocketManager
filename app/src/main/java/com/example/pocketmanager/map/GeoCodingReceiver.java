package com.example.pocketmanager.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.example.pocketmanager.general.APIListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class GeoCodingReceiver {
    private static Geocoder instance = null;
    protected static Context ctx;
    protected static Activity act;
    private static FusedLocationProviderClient fusedLocationClient;


    public static synchronized <Acticity> Geocoder getInstance(Activity context) {
        if (instance == null)
            instance = new Geocoder((Context) context, Locale.KOREA);
        act = context;
        ctx = (Context) context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized Geocoder getInstance() {
        if (instance == null) {
            throw new IllegalStateException(GeoCodingReceiver.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public static Address getAddressfromCoord(double latitude, double longitude) {
        try {
            List<Address> result = getInstance().getFromLocation(latitude, longitude, 1);
            if (result != null && result.size() > 0)
                return result.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Address getAddressfromName(String name) {
        try {
            List<Address> result = getInstance().getFromLocationName(name, 1);
            if (result != null && result.size() > 0)
                return result.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static LocationData getCurrentAddress(APIListener<LinkedList<Double>> listener) {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(act, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LinkedList<Double> result = new LinkedList<>();
                            result.add(location.getLatitude());
                            result.add(location.getLongitude());
                            listener.getResult(result);
                        }
                        else
                            System.out.println("");
                    }
                });

        return LocationData.getCurrentLocation();
    }
}
