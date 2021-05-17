package com.example.pocketmanager.ui.transporation;

import java.util.concurrent.ExecutionException;

public class PathInfoManager {
    // google direction api 호출을 위한 변수들
    private String origin;      // 시작지점 위도, 경도
    private String destination; // 도착지점 위도, 경도
    private String mode = "transit";       // 이동방법
    private String departure_time = "now"; // 출발 시간
    private String language = "ko";        // 받는 정보의 언어
    private String googleApiKey = "AIzaSyD8wydCmxm4OVhOH1Yedy39C-o_ypgk48I";

    // 지하철 api 호출을 위한 변수들
    private String subwayApiKey = "625754674e6a6a693136524a437743";
    private String subwayName; // 역 이름


    public String getShortestPathInfo() throws ExecutionException, InterruptedException {
        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin
                + "&destination=" + destination
                + "&mode=" + mode
                + "&departure_time=" + departure_time
                + "&language=" + language
                + "&key=" + googleApiKey;

        String pathInfo = new ApiCallTask().execute(url).get();

        return pathInfo;
    }

    public String getSubwayInfo() throws ExecutionException, InterruptedException {
        String url = "http://swopenAPI.seoul.go.kr/api/subway/" + subwayApiKey + "/json" +
                "/realtimeStationArrival/0/100/" + subwayName;

        String subwayInfo = new ApiCallTask().execute(url).get();

        return subwayInfo;
    }

    // getters and setters
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGoogleApiKey() {
        return googleApiKey;
    }

    public void setGoogleApiKey(String googleApiKey) {
        this.googleApiKey = googleApiKey;
    }

    public String getSubwayApiKey() {
        return subwayApiKey;
    }

    public void setSubwayApiKey(String subwayApiKey) {
        this.subwayApiKey = subwayApiKey;
    }

    public String getSubwayName() {
        return subwayName;
    }

    public void setSubwayName(String subwayName) {
        this.subwayName = subwayName;
    }


}
