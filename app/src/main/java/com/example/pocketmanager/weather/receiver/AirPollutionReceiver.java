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
import com.example.pocketmanager.general.LocationData;
import com.example.pocketmanager.weather.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class AirPollutionReceiver {
    private static AirPollutionReceiver instance = null;
    protected RequestQueue requestQueue;
    protected static Context ctx;

    private final String prefixURL = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?";
    private final String API_KEY = BuildConfig.WEATHER_KEY;

    private AirPollutionReceiver(Context context) {
        ctx = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        //other stuf if you need

    }

    public static synchronized AirPollutionReceiver getInstance(Context context) {
        if (instance == null)
            instance = new AirPollutionReceiver(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized AirPollutionReceiver getInstance() {
        if (null == instance) {
            throw new IllegalStateException(DailyWeatherReceiver.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void getAirPollution(final APIListener<LinkedList<WeatherData>> listener) {

        Double latitude = LocationData.getCurrentLocation().getLatitude();
        Double longitude = LocationData.getCurrentLocation().getLongitude();

        //https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat=57&lon=127&appid=b3a048704c9b2c9c25bd3d24b571d042
        StringBuilder builder = new StringBuilder(prefixURL);
        builder.append("lat=" + latitude + "&lon=" +  longitude);
        builder.append("&appid=" + API_KEY);
        String url = builder.toString();

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Display
                        try {
                            JSONArray jsonArray = response.getJSONArray("list");
                            LinkedList<WeatherData> result = new LinkedList<WeatherData>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                WeatherData data = new WeatherData();

                                data.setDt(obj.getLong("dt"));
                                data.setPm2_5((float) obj.getJSONObject("components").getDouble("pm2_5"));
                                data.setPm10((float) obj.getJSONObject("components").getDouble("pm10"));

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
