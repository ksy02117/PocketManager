package com.example.pocketmanager.ui.transporation;

import android.util.Log;

import java.util.ArrayList;

public class ShortestPath {
    // 시각
    private String timeZone;
    private String departureTime;
    private String departureTimeValue;
    private String arrivalTime;
    private String arrivalTimeValue;
    // 거리
    private String distance;
    private String distanceValue;
    // 걸리는 시간
    private String duration;
    private String durationValue;
    // 시작 위치
    private String startAddress;
    private String startLocationLatitude;
    private String startLocationLongitude;
    // 도착 위치
    private String endAddress;
    private String endLocationLatitude;
    private String endLocationLongitude;
    // 최단경로 이동 단계
    private ArrayList<ShortestPathStep> steps;

    public void log() {
        Log.d("departureTime", departureTime);
        Log.d("arrivalTime", arrivalTime);
        Log.d("distance", distance);
        Log.d("duration", duration);
        for (ShortestPathStep s : steps) s.log();
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
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

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
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

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
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

    public ArrayList<ShortestPathStep> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<ShortestPathStep> steps) {
        this.steps = steps;
    }
}
