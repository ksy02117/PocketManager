package com.example.pocketmanager.transportation;

import android.util.Log;

import java.text.SimpleDateFormat;

public class IncomingTrain {
    private String direction;   // 열차 방향(행선지)
    private String name;        // 열차 이름
    private String arriveTime;  // 열차 도착 시각
    private String stationName; // 정보를 얻은 역의 이름
    private Double stationId;   // 역id
    private String destinationArriveTime; // 이 열차을 탔을때 목적지 도착 시간

    public void log() {
        Log.d("name", name);
        Log.d("direction", direction);
        Log.d("arriveTime", arriveTime);
        Log.d("stationName", stationName);
        Log.d("stationId", Double.toString(stationId));
        Log.d("destinationArriveTime", destinationArriveTime);
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Double getStationId() {
        return stationId;
    }

    public void setStationId(Double stationId) {
        this.stationId = stationId;
    }

    public String getDestinationArriveTime() {
        return destinationArriveTime;
    }

    public void setDestinationArriveTime(String destinationArriveTime) {
        this.destinationArriveTime = destinationArriveTime;
    }


}
