package com.example.pocketmanager.weather.receiver;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pocketmanager.BuildConfig;
import com.example.pocketmanager.general.APIListener;
import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.weather.DailyWeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class DailyWeatherReceiver {
    private static DailyWeatherReceiver instance = null;
    protected RequestQueue requestQueue;
    protected static Context ctx;

    private final String prefixURL = "https://api.openweathermap.org/data/2.5/onecall?";
    private final String API_KEY = BuildConfig.WEATHER_KEY;


    private DailyWeatherReceiver(Context context) {
        ctx = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }


    public static synchronized DailyWeatherReceiver getInstance(Context context) {
        if (instance == null)
            instance = new DailyWeatherReceiver(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized DailyWeatherReceiver getInstance() {
        if (null == instance) {
            throw new IllegalStateException(DailyWeatherReceiver.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void getDailyWeather(final APIListener<LinkedList<DailyWeatherData>> listener) {

        Double latitude = LocationData.getCurrentLocation().getLatitude();
        Double longitude = LocationData.getCurrentLocation().getLongitude();


        //"https://api.openweathermap.org/data/2.5/onecall?q=seoul&lang=kr&units=metric&exclude=current,minutely,hourly&appid=b3a048704c9b2c9c25bd3d24b571d042"
        StringBuilder builder = new StringBuilder(prefixURL);
        builder.append("lat=" + latitude + "&lon=" + longitude);
        builder.append("&lang=kr");
        builder.append("&units=metric");
        builder.append("&exclude=current,minutely,hourly");
        builder.append("&appid=" + API_KEY);
        String url = builder.toString();


        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Display
                        try {
                            JSONArray jsonArray = response.getJSONArray("daily");
                            LinkedList<DailyWeatherData> result = new LinkedList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                DailyWeatherData data = new DailyWeatherData();

                                data.setDt(obj.getLong("dt"));

                                //main
                                data.setMax_temp((float) obj.getJSONObject("temp").getDouble("max"));
                                data.setMin_temp((float) obj.getJSONObject("temp").getDouble("min"));

                                //weather
                                data.setWeather(obj.getJSONArray("weather").getJSONObject(0).getString("main"));
                                data.setIcon(obj.getJSONArray("weather").getJSONObject(0).getString("icon"));
                                data.setPop((float) obj.getDouble("pop"));

                                result.add(data);
                            }

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
}
