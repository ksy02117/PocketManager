package com.example.pocketmanager.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pocketmanager.storage.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AirPollutionReceiver extends OpenWeatherMapReceiver{
    private static AirPollutionReceiver instance = null;
    protected RequestQueue requestQueue;
    protected static Context ctx;

    private final String prefixURL = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?";

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
            throw new IllegalStateException(WeatherReceiver.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void getCurrentLocationAirPollution(float latitude, float longitude, final OpenWeatherMapListener listener) {

        //https://api.openweathermap.org/data/2.5/air_pollution/history?lat=57&lon=127&appid=b3a048704c9b2c9c25bd3d24b571d042


        StringBuilder builder = new StringBuilder(prefixURL);
        builder.append(String.format("lat=%f&lon=%f", latitude, longitude));
        builder.append("&appid=b3a048704c9b2c9c25bd3d24b571d042");
        String airPollutionURL = builder.toString();

        // Request a string response from the provided URL.
        JsonObjectRequest airPollutionRequest = new JsonObjectRequest(Request.Method.GET, airPollutionURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Display
                        try {
                            JSONArray jsonAirPollutionArray = response.getJSONArray("list");

                            for (int i = 0, j = 0; i < jsonAirPollutionArray.length() && j < WeatherData.currentLocationWeatherData.size(); i++) {
                                JSONObject obj = jsonAirPollutionArray.getJSONObject(i);
                                WeatherData data = WeatherData.currentLocationWeatherData.get(j);
                                if (obj.getLong("dt") == data.getDt()) {
                                    data.setPm2_5((float) obj.getJSONObject("components").getDouble("pm2_5"));
                                    data.setPm10((float) obj.getJSONObject("components").getDouble("pm10"));
                                    j++;
                                }
                            }

                            listener.postRequest(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.postRequest(false);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error
                error.printStackTrace();
                listener.postRequest(false);
            }
        });
        requestQueue.add(airPollutionRequest);
    }
}
