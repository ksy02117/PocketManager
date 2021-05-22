package com.example.pocketmanager.ui.transporation;

import android.util.Log;

public class ShortestPathStep { // 최단경로를 이루는 단계
    // 거리
    private String distance;
    private String distanceValue;
    // 걸리는 시간
    private String duration;
    private String durationValue;
    // 설명
    private String htmlInstruction;
    // 시작 위치
    private String startLocationLatitude;
    private String startLocationLongitude;
    // 도착 위치
    private String endLocationLatitude;
    private String endLocationLongitude;
    // 이동 방식 ("WALKING" or "TRANSIT")
    private String travelMode;

    // 지하철로 이동하는 단계일때 부가정보
    // 출발
    private String departureTime;
    private String departureTimeValue;
    private String departureStopName;
    // 도착
    private String arrivalTime;
    private String arrivalTimeValue;
    private String arrivalStopName;
    // 거쳐가는 역 갯수
    private String numStops;

    public void log() {
        Log.d("instruction", htmlInstruction);
        Log.d("travelMode", travelMode);
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistanceValue() {
        return distanceValue;
    }

    public void setDistanceValue(String distanceValue) {
        this.distanceValue = distanceValue;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(String durationValue) {
        this.durationValue = durationValue;
    }

    public String getHtmlInstruction() {
        return htmlInstruction;
    }

    public void setHtmlInstruction(String htmlInstruction) {
        this.htmlInstruction = htmlInstruction;
    }

    public String getStartLocationLatitude() {
        return startLocationLatitude;
    }

    public void setStartLocationLatitude(String startLocationLatitude) {
        this.startLocationLatitude = startLocationLatitude;
    }

    public String getStartLocationLongitude() {
        return startLocationLongitude;
    }

    public void setStartLocationLongitude(String startLocationLongitude) {
        this.startLocationLongitude = startLocationLongitude;
    }

    public String getEndLocationLatitude() {
        return endLocationLatitude;
    }

    public void setEndLocationLatitude(String endLocationLatitude) {
        this.endLocationLatitude = endLocationLatitude;
    }

    public String getEndLocationLongitude() {
        return endLocationLongitude;
    }

    public void setEndLocationLongitude(String endLocationLongitude) {
        this.endLocationLongitude = endLocationLongitude;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureTimeValue() {
        return departureTimeValue;
    }

    public void setDepartureTimeValue(String departureTimeValue) {
        this.departureTimeValue = departureTimeValue;
    }

    public String getDepartureStopName() {
        return departureStopName;
    }

    public void setDepartureStopName(String departureStopName) {
        this.departureStopName = departureStopName;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getArrivalTimeValue() {
        return arrivalTimeValue;
    }

    public void setArrivalTimeValue(String arrivalTimeValue) {
        this.arrivalTimeValue = arrivalTimeValue;
    }

    public String getArrivalStopName() {
        return arrivalStopName;
    }

    public void setArrivalStopName(String arrivalStopName) {
        this.arrivalStopName = arrivalStopName;
    }

    public String getNumStops() {
        return numStops;
    }

    public void setNumStops(String numStops) {
        this.numStops = numStops;
    }
}
