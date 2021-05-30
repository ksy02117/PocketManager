package com.example.pocketmanager.map.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.MainActivity;
import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.transportation.ShortestPath;
import com.example.pocketmanager.transportation.ShortestPathStep;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MapFragment extends Fragment implements MapView.MapViewEventListener {
    private View view;
    private MapView mapView;
    private MapPolyline polyline;
    private ShortestPath path;
    private ArrayList<ShortestPathStep> steps;
    private MapPOIItem selectedLocationMarker = null;
    private Double selectLatitude;
    private Double selectLongitude;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map, container, false);

        // 지도 생성후 보여주기
        initMapView();

        // 화면 중심점 옮기기
        //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.546988, 127.105476), true);

        // 지도에 최단경로를 그리기위한 polyline 객체 생성
        initPolyline();

        // MainActivity에서 ShortestPath 객체를 전달받음
        getShortestPath();

        // map objects 초기화
        mapView.removeAllPOIItems();
        mapView.removeAllPolylines();

        // event 등록
        mapView.setMapViewEventListener(this);

        // 만약 정보를 불러오는데 실패했다면 최단 경로를 출력하지 않음
        if (steps == null) {
            // 현재 위치로 지도 중심 옮기기
            //TODO gps
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(LocationData.getCurrentLocation().getLatitude(), LocationData.getCurrentLocation().getLongitude()), false);
            return view;
        }

        // for-each문으로 steps를 순회하며 목적지의 위치를 추가
        ShortestPathStep firstStep = steps.get(0);
        for (ShortestPathStep s : steps){
            // point 생성
            Double lat = Double.parseDouble(s.getStartLocationLatitude());
            Double lng = Double.parseDouble(s.getStartLocationLongitude());
            MapPoint point = MapPoint.mapPointWithGeoCoord(lat, lng);

            // polyline에 추가
            polyline.addPoint(point);
            // 마커 색 정하기
            MapPOIItem.MarkerType markerType;
            if (s == firstStep) markerType = MapPOIItem.MarkerType.BluePin; // 첫위치는 파랑색
            else markerType = MapPOIItem.MarkerType.YellowPin;              // 이외에는 노랑색
            // 지하철이나 도보에 맞게 name 변형
            String name = "";
            if (s.getTravelMode().equals("TRANSIT") && s.getTransportationType().equals("SUBWAY")) name = "지하철 " + s.getArrivalStopName() + "행";
            else name = stepNameToEffectiveName(s.getHtmlInstruction());
            // 마커 그리기
            addAndDrawMarker(mapView, name, point, markerType);
        }

        // 끝 추가
        MapPoint point = MapPoint.mapPointWithGeoCoord(Double.parseDouble(path.getEndLocationLatitude()), Double.parseDouble(path.getEndLocationLongitude()));
        polyline.addPoint(point);
        addAndDrawMarker(mapView, "목적지", point,MapPOIItem.MarkerType.RedPin);

        // 폴리라인 그리기
        mapView.addPolyline(polyline);
        // 모든 폴리라인과 마커가 나오도록 위치 조정
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

        return view;
    }

    private void initMapView() {
        mapView = new MapView(getActivity());
        mapView.setMapTilePersistentCacheEnabled(true);

        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);

    }

    private void initPolyline() {
        polyline = new MapPolyline();
        polyline.setTag(0);
        polyline.setLineColor(Color.argb(255, 243, 100, 0)); // Polyline 컬러 지정.

    }

    private void getShortestPath() {
        Bundle bundle = getArguments();
        path = (ShortestPath) bundle.getSerializable("ShortestPath");
        steps = path.getSteps();
    }


    private MapPOIItem addAndDrawMarker(MapView m, String name, MapPoint p, MapPOIItem.MarkerType t) {
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(name);
        marker.setTag(0);
        marker.setMapPoint(p);
        marker.setMarkerType(t); // 마커 색
        m.addPOIItem(marker);

        return marker;
    }

    private String stepNameToEffectiveName(String stepName) {
        if (stepName.length() < 4) return stepName; //예외처리
        // 나라이름 제거
        if (stepName.substring(0, 4).equals("대한민국")) stepName = stepName.replaceFirst("대한민국 ", "");

        if (stepName.length() < 5) return stepName; //예외처리
        // 시 제거
        if (stepName.substring(0, 5).equals("서울특별시")) stepName = stepName.replaceFirst("서울특별시 ", "");
        else if (stepName.substring(0, 3).equals("경기도")) stepName = stepName.replaceFirst("경기도 ", "");
        else if (stepName.substring(0, 3).equals("강원도")) stepName = stepName.replaceFirst("강원도 ", "");
        else if (stepName.substring(0, 4).equals("충청북도")) stepName = stepName.replaceFirst("충청북도 ", "");
        else if (stepName.substring(0, 4).equals("충청남도")) stepName = stepName.replaceFirst("충청남도 ", "");
        else if (stepName.substring(0, 4).equals("경상북도")) stepName = stepName.replaceFirst("경상북도 ", "");
        else if (stepName.substring(0, 4).equals("경상남도")) stepName = stepName.replaceFirst("경상남도 ", "");
        else if (stepName.substring(0, 4).equals("전라북도")) stepName = stepName.replaceFirst("전라북도 ", "");
        else if (stepName.substring(0, 4).equals("전라남도")) stepName = stepName.replaceFirst("전라남도 ", "");

        if (stepName.length() < 3) return stepName; //예외처리
        // 동 제거
        if (stepName.substring(0, 3).equals("군자동")) stepName = stepName.replaceFirst("군자동 ", "");

        return stepName;

    }

    public void getTouchedLocation(MapPoint mapPoint) {
        selectLatitude = mapPoint.getMapPointGeoCoord().latitude;
        selectLongitude = mapPoint.getMapPointGeoCoord().latitude;
        Log.d("touched_location", selectLatitude.toString() + ", " + selectLongitude.toString());

        if (selectedLocationMarker != null) mapView.removePOIItem(selectedLocationMarker);
        selectedLocationMarker = addAndDrawMarker(mapView, "선택된 위치", mapPoint, MapPOIItem.MarkerType.RedPin);
    }



    @Override
    public void onMapViewInitialized(MapView mapView) {
        //Log.d("MapViewInitialized", "MapViewInitialized");
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        //Log.d("ASD", Double.toString(mapPoint.getMapPointGeoCoord().latitude));
        //mapView.removePOIItem(selectedLocationMarker);
        getTouchedLocation(mapPoint);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        //getTouchedLocation(mapPoint);
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}