package com.example.pocketmanager.schedule.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Timer;
import java.util.TimerTask;

public class addLocationActivity extends Activity implements MapView.MapViewEventListener, TextView.OnEditorActionListener { // 이벤트를 추가할때 위치를 추가하는 activity입니다.
    private View view;
    ViewGroup mapViewContainer;
    private MapView mapView;
    private EditText locationSearchBar;
    private Button cancel, confirm;
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

        cancel = (Button) findViewById(R.id.add_location_cancel);
        confirm = (Button) findViewById(R.id.add_location_confirm);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                if (selectLatitude == null)
                    return;

                // 데이터 내보내기
                intent.putExtra("name", locationSearchBar.getText().toString());
                intent.putExtra("latitude", selectLatitude);
                intent.putExtra("longitude", selectLongitude);


                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // 지도 초기화 및 생성
        initMapView();
        locationSearchBar = (EditText) findViewById(R.id.location_search_bar);

        // 이벤트 등록
        mapView.setMapViewEventListener(this);

        // 현재 위치로 지도 중심 옮기기
        //TODO gps
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(LocationData.getCurrentLocation().getLatitude(), LocationData.getCurrentLocation().getLongitude()), false);

        locationSearchBar.setOnEditorActionListener(this);
        locationSearchBar.setFocusable(true);
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


    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) { }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        getLocation(mapPoint);
    }

    public void locationNameToAddress(String locationName) { // 검색하려하는 문자열을 받아 위치로 변환하여 화면을 전환하고 마커를 찍음
        Address a = GeoCodingReceiver.getAddressfromName(locationName);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(a.getLatitude(), a.getLongitude()), true);
        //getLocation(MapPoint.mapPointWithGeoCoord(a.getLatitude(), a.getLongitude()));
        mapView.requestFocus();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == KeyEvent.KEYCODE_ENDCALL) {
            locationNameToAddress(v.getText().toString());
            hideKeyboard(this);
            return true;
        }

        return false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onMapViewInitialized(MapView mapView) { }
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) { }

}
