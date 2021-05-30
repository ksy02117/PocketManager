package com.example.pocketmanager.schedule.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.map.GeoCodingReceiver;
import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.schedule.storage.Event;
import com.example.pocketmanager.schedule.storage.SubEvent;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class addLocationActivity extends Activity implements MapView.MapViewEventListener { // 이벤트를 추가할때 위치를 추가하는 activity입니다.
    private View view;
    private MapView mapView;
    private MapPOIItem selectedLocationMarker = null;
    private Double selectLatitude;
    private Double selectLongitude;

    public addLocationActivity() { }
    public static addLocationActivity getInstance() {
        addLocationActivity e = new addLocationActivity();

        return e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_location);

        // 지도 초기화 및 생성
        initMapView();

        // 이벤트 등록
        mapView.setMapViewEventListener(this);

        // 현재 위치로 지도 중심 옮기기
        //TODO gps
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(LocationData.getCurrentLocation().getLatitude(), LocationData.getCurrentLocation().getLongitude()), false);

    }

    private void initMapView() { // 지도 초기화 및 위치
        mapView = new MapView(this);
        mapView.setMapTilePersistentCacheEnabled(true);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.mapView2);
        mapViewContainer.addView(mapView);

    }

    private MapPOIItem addAndDrawMarker(MapView m, String name, MapPoint p, MapPOIItem.MarkerType t) {
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(name); // 마커 설명
        marker.setTag(0);         // 탭 번호
        marker.setMapPoint(p);    // 위치
        marker.setMarkerType(t);  // 마커 색
        m.addPOIItem(marker);

        return marker;
    }

    public void getLocation(MapPoint mapPoint) { // mapPoint를 받아 위치를 가져옴
        selectLatitude = mapPoint.getMapPointGeoCoord().latitude;
        selectLongitude = mapPoint.getMapPointGeoCoord().latitude;
        Log.d("touched_location", selectLatitude.toString() + ", " + selectLongitude.toString());

        if (selectedLocationMarker != null) mapView.removePOIItem(selectedLocationMarker);
        selectedLocationMarker = addAndDrawMarker(mapView, "선택된 위치", mapPoint, MapPOIItem.MarkerType.RedPin);
        mapView.setMapCenterPoint(mapPoint, false);
    }

    public void locationNameToAddress(String locationName) { // 검색하려하는 문자열을 받아 위치로 변환하여 화면을 전환하고 마커를 찍음
        Address a = GeoCodingReceiver.getAddressfromName(locationName);
        getLocation(MapPoint.mapPointWithGeoCoord(a.getLatitude(), a.getLongitude()));
    }


    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        getLocation(mapPoint);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

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