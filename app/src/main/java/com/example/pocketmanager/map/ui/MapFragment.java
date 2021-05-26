package com.example.pocketmanager.map.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pocketmanager.R;
import com.example.pocketmanager.transportation.ShortestPath;
import com.example.pocketmanager.transportation.ShortestPathStep;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map, container, false);

        // 지도 생성후 보여주기
        MapView mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);

        // 화면 중심점 옮기기
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.546988, 127.105476), true);

        // 지도에 최단경로를 그리기위한 polyline 객체
        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(192, 255, 51, 0)); // Polyline 컬러 지정.

        // MainActivity에서 ShortestPath 객체를 전달받음
        Bundle bundle = getArguments();
        ShortestPath path = (ShortestPath) bundle.getSerializable("Obj");
        ArrayList<ShortestPathStep> steps = path.getSteps();

        // 시작점 추가
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(path.getStartLocationLatitude()), Double.parseDouble(path.getStartLocationLongitude())));
        // for-each문으로 steps를 순회하며 목적지의 위치를 추가
        for (ShortestPathStep s : steps){
            Double lat = Double.parseDouble(s.getEndLocationLatitude());
            Double lng = Double.parseDouble(s.getEndLocationLongitude());
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
        }
        // 그리기
        mapView.addPolyline(polyline);

        return view;
    }
}