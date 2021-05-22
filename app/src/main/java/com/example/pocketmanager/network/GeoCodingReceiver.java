package com.example.pocketmanager.network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pocketmanager.BuildConfig;
import com.example.pocketmanager.storage.LocationData;
import com.example.pocketmanager.storage.WeatherData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GeoCodingReceiver {
    private static Geocoder instance = null;
    protected static Context ctx;
    protected static Activity act;
    private static FusedLocationProviderClient fusedLocationClient;


    public static synchronized <Acticity> Geocoder getInstance(Activity context) {
        if (instance == null)
            instance = new Geocoder((Context) context, Locale.US);
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


    public static LocationData getCurrentAddress() {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(act, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Logic to handle location object
                            LocationData.setCurrentLocation(location.getLatitude(), location.getLongitude());
                        }
                    }
                });

        return LocationData.getCurrentLocation();
    }
}
