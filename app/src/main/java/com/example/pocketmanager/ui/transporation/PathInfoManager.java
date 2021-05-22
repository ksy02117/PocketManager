package com.example.pocketmanager.ui.transporation;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PathInfoManager {
    // google direction api 호출을 위한 변수들
    private String origin;      // 시작지점 위도, 경도
    private String destination; // 도착지점 위도, 경도
    private String mode = "transit";       // 이동방법
    private String departure_time = "now"; // 출발 시간
    private String language = "ko";        // 받는 정보의 언어
    private String prefer = "subway";      // 선호 교통수단
    private String googleApiKey = "AIzaSyD8wydCmxm4OVhOH1Yedy39C-o_ypgk48I";

    // 지하철 api 호출을 위한 변수들
    private String subwayApiKey = "625754674e6a6a693136524a437743";
    private String subwayName; // 역 이름


    public ShortestPath getShortestPathInfo() throws ExecutionException, InterruptedException {
        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin
                + "&destination=" + destination
                + "&mode=" + mode
                + "&departure_time=" + departure_time
                + "&language=" + language
                + "&transit_mode=" + prefer
                + "&key=" + googleApiKey;

        String pathInfo = new ApiCallTask().execute(url).get();

        JSONObject jObject;
        JSONArray jArray;

        ShortestPath shortestPath = new ShortestPath();
        ArrayList<ShortestPathStep> shortestPathSteps = new ArrayList<ShortestPathStep>();
        try {
            jObject = new JSONObject(pathInfo);
            jArray = jObject.getJSONArray("routes");
            jObject = jArray.getJSONObject(0);
            jArray = jObject.getJSONArray("legs");
            jObject = jArray.getJSONObject(0);

            // 시각 설정
            shortestPath.setTimeZone(jObject.getJSONObject("arrival_time").getString("time_zone"));
            shortestPath.setArrivalTime(jObject.getJSONObject("arrival_time").getString("text"));
            shortestPath.setArrivalTimeValue(jObject.getJSONObject("arrival_time").getString("value"));
            shortestPath.setDepartureTime(jObject.getJSONObject("departure_time").getString("text"));
            shortestPath.setDepartureTimeValue(jObject.getJSONObject("departure_time").getString("value"));
            // 걸리는 거리 설정
            shortestPath.setDistance(jObject.getJSONObject("distance").getString("text"));
            shortestPath.setDistanceValue(jObject.getJSONObject("distance").getString("value"));
            // 걸리는 시간 설정
            shortestPath.setDuration(jObject.getJSONObject("duration").getString("text"));
            shortestPath.setDurationValue(jObject.getJSONObject("duration").getString("value"));
            // 시작 위치 설정
            shortestPath.setStartAddress(jObject.getString("start_address"));
            shortestPath.setStartLocationLatitude(jObject.getJSONObject("start_location").getString("lat"));
            shortestPath.setStartLocationLongitude(jObject.getJSONObject("start_location").getString("lng"));
            // 도착 위치 설정
            shortestPath.setEndAddress(jObject.getString("end_address"));
            shortestPath.setEndLocationLatitude(jObject.getJSONObject("end_location").getString("lat"));
            shortestPath.setEndLocationLongitude(jObject.getJSONObject("end_location").getString("lng"));

            jArray = jObject.getJSONArray("steps");

            for (int i = 0 ; i < jArray.length() ; i++) { // 단계들을 하나씩 얻어옴
                JSONObject obj = jArray.getJSONObject(i);
                ShortestPathStep shortestPathStep = new ShortestPathStep();
                // 거리 설정
                shortestPathStep.setDistance(obj.getJSONObject("distance").getString("text"));
                shortestPathStep.setDistanceValue(obj.getJSONObject("distance").getString("value"));
                // 걸리는 시간 설정
                shortestPathStep.setDuration(obj.getJSONObject("duration").getString("text"));
                shortestPathStep.setDurationValue(obj.getJSONObject("duration").getString("value"));
                // 설명 설정
                shortestPathStep.setHtmlInstruction(obj.getString("html_instructions"));
                // 시작 위치 설정
                shortestPathStep.setStartLocationLatitude(obj.getJSONObject("start_location").getString("lat"));
                shortestPathStep.setStartLocationLongitude(obj.getJSONObject("start_location").getString("lng"));
                // 도착 위치 설정
                shortestPathStep.setEndLocationLatitude(obj.getJSONObject("end_location").getString("lat"));
                shortestPathStep.setEndLocationLongitude(obj.getJSONObject("end_location").getString("lng"));
                // 이동 방식 설정
                shortestPathStep.setTravelMode(obj.getString("travel_mode"));

                if (shortestPathStep.getTravelMode() == "TRANSIT"){ //transit_details
                    JSONObject tmp = obj.getJSONObject("transit_details");
                    // 출발 정보 설정
                    shortestPathStep.setDepartureTime(tmp.getJSONObject("departure_time").getString("text"));
                    shortestPathStep.setDepartureTimeValue(tmp.getJSONObject("departure_time").getString("value"));
                    shortestPathStep.setDepartureStopName(tmp.getJSONObject("departure_stop").getString("name"));
                    // 도착 정보 설정
                    shortestPathStep.setArrivalTime(tmp.getJSONObject("arrival_time").getString("text"));
                    shortestPathStep.setArrivalTimeValue(tmp.getJSONObject("arrival_time").getString("value"));
                    shortestPathStep.setArrivalStopName(tmp.getJSONObject("arrival_stop").getString("name"));
                    // 거쳐가는 역 갯수 설정
                    shortestPathStep.setNumStops(tmp.getString("num_stops"));
                }

                shortestPathSteps.add(shortestPathStep);
            }
            shortestPath.setSteps(shortestPathSteps);
            shortestPath.log();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return shortestPath;
    }

    public ArrayList<IncommingTrain> getIncomingTrainInfo() throws ExecutionException, InterruptedException {
        String url = "http://swopenAPI.seoul.go.kr/api/subway/" + subwayApiKey + "/json" +
                "/realtimeStationArrival/0/100/" + subwayName;

        String subwayInfo = new ApiCallTask().execute(url).get();

        JSONObject jObject;
        JSONArray jArray;
        ArrayList<IncommingTrain> incommingTrains = new ArrayList<IncommingTrain>();
        try {
            jObject = new JSONObject(subwayInfo);
            Log.d("subway_api_message", jObject.getString("errorMessage"));

            jArray = jObject.getJSONArray("realtimeArrivalList"); // 오는 지하철 정보에 대한 JSON배열 얻기

            for (int i = 0 ; i < jArray.length() ; i++){
                JSONObject obj = jArray.getJSONObject(i);
                String direction = obj.getString("updnLine"); // 방향
                String name = obj.getString("trainLineNm");   // 이름
                String barvlDt = obj.getString("barvlDt");    // 예상 시간
                String recptnDt = obj.getString("recptnDt");  // 데이터 생성 시각

                java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(recptnDt);

                java.sql.Date date = new java.sql.Date(timestamp.getTime() + Long.parseLong(barvlDt) * 1000); // 예상되는 도착 시각 생성

                // train 객체 생성후 얻은 정보들로 초기화
                IncommingTrain train = new IncommingTrain();
                train.setDirection(direction);
                train.setName(name);
                train.setArriveTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
                //리스트에 추가
                incommingTrains.add(train);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return incommingTrains;
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
