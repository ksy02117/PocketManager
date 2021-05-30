package com.example.pocketmanager.map.ui;

import android.util.Log;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class TapEventListner implements MapView.MapViewEventListener {

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Log.d("QWE","ASDASD");
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        Log.d("QWE","ASDASD");
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        Log.d("QWE","ASDASD");
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        Log.d("QWE","ASDASD");
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        Log.d("QWE","ASDASD");
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        Log.d("QWE","ASDASD");
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        Log.d("QWE","ASDASD");
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Log.d("QWE","ASDASD");
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        Log.d("QWE","ASDASD");
    }
}
