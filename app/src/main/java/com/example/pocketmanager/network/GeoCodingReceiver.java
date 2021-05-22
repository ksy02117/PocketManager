package com.example.pocketmanager.network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.example.pocketmanager.storage.LocationData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

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



    /*
    public void getAddress(float latitude, float longitude, final APIListener<String> listener) {

        StringBuilder builder = new StringBuilder(prefixURL);
        builder.append("latlng=" + latitude + "," + longitude);
        builder.append("language=kr");
        builder.append("&key=" + API_KEY);
        String url = builder.toString();

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Display
                        try {
                            JSONObject jsonData = response.getJSONArray("results").getJSONObject(0);

                            String result = jsonData.getString("formatted_address");
                            listener.getResult(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.getResult(null);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error
                error.printStackTrace();
                listener.getResult(null);
            }
        });
        requestQueue.add(request);
    }

    public void getCoordByAddress(String address, final APIListener<HashMap<String, Float>> listener) {

        StringBuilder builder = new StringBuilder(prefixURL);
        address.replaceAll(" ", ",");
        builder.append("address=" + address);
        builder.append("&appid=" + API_KEY);
        String url = builder.toString();

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Display
                        try {
                            JSONObject jsonData = response.getJSONArray("list").getJSONObject(0);

                            float latitude = (float) jsonData.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            float longitude = (float) jsonData.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            HashMap<String, Float> result = new HashMap<>();
                            result.put("lat", latitude);
                            result.put("long", longitude);
                            listener.getResult(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.getResult(null);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error
                error.printStackTrace();
                listener.getResult(null);
            }
        });
        requestQueue.add(request);
    }
    */
}