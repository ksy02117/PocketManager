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

public class WeatherReceiver extends OpenWeatherMapReceiver {
    private static WeatherReceiver instance = null;
    protected RequestQueue requestQueue;
    protected static Context ctx;

    private final String prefixURL = "https://api.openweathermap.org/data/2.5/onecall?";


    private WeatherReceiver(Context context) {
        ctx = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        //other stuf if you need

    }

    public static synchronized WeatherReceiver getInstance(Context context) {
        if (instance == null)
            instance = new WeatherReceiver(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized WeatherReceiver getInstance() {
        if (null == instance) {
            throw new IllegalStateException(WeatherReceiver.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void getCurrentLocationWeather(float latitude, float longitude, final OpenWeatherMapListener listener) {

        //"https://api.openweathermap.org/data/2.5/forecast?q=seoul&lang=kr&appid=b3a048704c9b2c9c25bd3d24b571d042"
        StringBuilder builder = new StringBuilder(prefixURL);
        builder.append("lat=" + latitude + "&lon=" + longitude);
        builder.append("&lang=kr");
        builder.append("&units=metric");
        builder.append("&exclude=minutely,daily");
        builder.append("&appid=" + API_KEY);
        String weatherURL = builder.toString();


        // Request a string response from the provided URL.
        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, weatherURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Display
                        try {
                            JSONArray jsonWeatherArray = response.getJSONArray("hourly");

                            float latitude = (float) response.getDouble("lat");
                            float longitude = (float) response.getDouble("lon");

                            for (int i = 0, j = 0; i < jsonWeatherArray.length() && j < WeatherData.currentLocationWeatherData.size(); i++) {
                                JSONObject obj = jsonWeatherArray.getJSONObject(i);
                                WeatherData data = WeatherData.currentLocationWeatherData.get(j);

                                if (obj.getLong("dt") == data.getDt()) {
                                    //main
                                    data.setTemp((float) obj.getDouble("temp"));
                                    data.setFeels_like((float) obj.getDouble("feels_like"));
                                    data.setHumidity((float) obj.getDouble("humidity"));

                                    //weather
                                    data.setWeather(obj.getJSONArray("weather").getJSONObject(0).getString("main"));
                                    data.setDescription(obj.getJSONArray("weather").getJSONObject(0).getString("description"));
                                    data.setIcon(obj.getJSONArray("weather").getJSONObject(0).getString("icon"));
                                    data.setWind_speed((float) obj.getDouble("wind_speed"));

                                    if (obj.has("rain"))
                                        data.setRain_1h((float) obj.getJSONObject("rain").getDouble("1h"));
                                    else
                                        data.setRain_1h(0);
                                    if (obj.has("snow"))
                                        data.setSnow_1h((float) obj.getJSONObject("snow").getDouble("1h"));
                                    else
                                        data.setSnow_1h(0);

                                    data.setLatitude(latitude);
                                    data.setLongitude(longitude);
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
        requestQueue.add(weatherRequest);

    }
}
