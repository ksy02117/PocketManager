package com.example.pocketmanager.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pocketmanager.R;
import com.example.pocketmanager.storage.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {
    private ArrayList<WeatherItemData> itemList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private MyWeatherAdapter adapter;
    View view;

    String parsed;
    TextView tempTxt;
    RequestQueue mQueue;

    public WeatherFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.tenki,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.weather_by_time_list);

        itemList = WeatherItemData.createContactsList(8);

        recyclerView.setHasFixedSize(true);
        adapter = new MyWeatherAdapter(getActivity(), itemList);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(adapter);



        getWeather();

        return view;
    }

    public void getWeather() {
        tempTxt = (TextView) view.findViewById(R.id.current_temperature);

        // Instantiate the RequestQueue
        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //build url
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=seoul&appid=b3a048704c9b2c9c25bd3d24b571d042";

        tempTxt.setText("1q0");
        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tempTxt.setText("1q1");

                        // Display
                        try {
                            JSONArray jsonWeatherArray = response.getJSONArray("list");

                            List<WeatherData> weatherDatas = new ArrayList<>();

                            for (int i = 0, j = jsonWeatherArray.length(); i < j; i++) {
                                JSONObject obj = jsonWeatherArray.getJSONObject(i);
                                WeatherData weatherData = new WeatherData();

                                weatherData.setDt(obj.getInt("dt"));

                                weatherData.setTemp((float) obj.getJSONObject("main").getDouble("temp"));

                                weatherDatas.add(weatherData);
                            }

                            tempTxt.setText(weatherDatas.get(0).getTemp() + "Kelvin");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

}