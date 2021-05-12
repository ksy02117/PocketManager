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

import com.example.pocketmanager.R;

import java.util.ArrayList;

public class WeatherFragment extends Fragment {
    private ArrayList<WeatherItemData> itemList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private MyWeatherAdapter adapter;
    View view;

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

        return view;
    }
}