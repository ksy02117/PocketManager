package com.example.pocketmanager.transportation;

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
    private String subwayName = null; // 역 이름

    // 특정 시각에 도착하는 지하철을 탔을때 예상 도착시간을 계산하기 위한 변수
    private int headsign; // 종착역 번호
    private String durationValueBeforeTakeSubway; // 지하철 타기전 최단경로 걸리는 시간
    private String durationValueAfterTakeSubway;  // 지하철 탄 후 최단경로 걸리는 시간

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

                if (shortestPathStep.getTravelMode().equals("TRANSIT")){ //transit_details
                    JSONObject tmp = obj.getJSONObject("transit_details");
                    // 출발 정보 설정
                    shortestPathStep.setDepartureTime(tmp.getJSONObject("departure_time").getString("text"));
                    shortestPathStep.setDepartureTimeValue(tmp.getJSONObject("departure_time").getString("value"));
                    shortestPathStep.setDepartureStopName(tmp.getJSONObject("departure_stop").getString("name"));
                    if (subwayName == null) { // 특정 시각에 도착하는 지하철을 탔을때 예상 도착시간을 계산하기 위한 데이터 얻기
                        subwayName = changeODSaySubwayNameToTOPIS(shortestPathStep.getDepartureStopName());
                        headsign = Integer.parseInt(tmp.getString("headsign"));
                        int d = 0;
                        for (ShortestPathStep s : shortestPathSteps) d += Integer.parseInt(s.getDurationValue());
                        durationValueBeforeTakeSubway = Integer.toString(d);
                    }
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

            durationValueAfterTakeSubway = Integer.toString(Integer.parseInt(shortestPath.getDurationValue()) - Integer.parseInt(durationValueBeforeTakeSubway));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return shortestPath;
    }

    public ArrayList<IncomingTrain> getAllIncomingTrainInfo() throws ExecutionException, InterruptedException {
        String url = "http://swopenAPI.seoul.go.kr/api/subway/" + subwayApiKey + "/json" +
                "/realtimeStationArrival/0/100/" + subwayName;

        String subwayInfo = new ApiCallTask().execute(url).get();

        JSONObject jObject;
        JSONArray jArray;
        ArrayList<IncomingTrain> incommingTrains = new ArrayList<IncomingTrain>();
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
                String stationName = subwayName;
                Double stationId = getStationId(obj.getString("statnId"));

                java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(recptnDt);

                java.sql.Date date = new java.sql.Date(timestamp.getTime() + Long.parseLong(barvlDt) * 1000); // 예상되는 도착 시각 생성

                // train 객체 생성후 얻은 정보들로 초기화
                IncomingTrain train = new IncomingTrain();
                train.setDirection(direction);
                train.setName(name);
                train.setArriveTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                train.setStationName(stationName);
                train.setStationId(stationId);
                //리스트에 추가
                incommingTrains.add(train);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        subwayName = null;

        return incommingTrains;
    }

    public ArrayList<IncomingTrain> getIncomingTrainInfo() throws ExecutionException, InterruptedException {
        // 역으로 들어오는 모든 지하철 정보를 얻어옴
        ArrayList<IncomingTrain> incomingTrains = getAllIncomingTrainInfo();
        ArrayList<IncomingTrain> result = new ArrayList<IncomingTrain>();
        String direction = null;
        if (incomingTrains.get(0).getStationId() < headsign) direction = "하행";
        else direction = "상행";
        for (IncomingTrain t : incomingTrains){
            if (t.getDirection().equals(direction)) {
                Log.d("qwe", t.getArriveTime());
                java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(t.getArriveTime());
                java.sql.Date date = new java.sql.Date(timestamp.getTime() + Long.parseLong(durationValueAfterTakeSubway) * 1000); // 예상되는 도착 시각 생성
                t.setDestinationArriveTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                result.add(t);
            }
        }

        return result;

    }


    private String changeODSaySubwayNameToTOPIS(String ODSay) {
        String TOPIS = ODSay;
        if (ODSay.equals("올림픽공원")) TOPIS = "올림픽공원(한국체대)";
        else if (ODSay.equals("월드컵경기장")) TOPIS = "월드컵경기장(성산)";
        else if (ODSay.equals("대흥")) TOPIS = "대흥(서강대앞)";
        else if (ODSay.equals("공릉")) TOPIS = "공릉(서울산업대입구)";
        else if (ODSay.equals("숭실대입구")) TOPIS = "숭실대입구(살피재)";
        else if (ODSay.equals("군자")) TOPIS = "군자(능동)";
        else if (ODSay.equals("천호")) TOPIS = "천호(풍납토성)";
        else if (ODSay.equals("굽은다리")) TOPIS = "굽은다리(강동구민회관앞)";
        else if (ODSay.equals("남한산성입구")) TOPIS = "남한산성입구(성남법원, 검찰청)";
        else if (ODSay.equals("오목교")) TOPIS = "오목교(목동운동장앞)";
        else if (ODSay.equals("몽촌토성")) TOPIS = "몽촌토성(평화의문)";
        else if (ODSay.equals("증산")) TOPIS = "증산(명지대앞)";
        else if (ODSay.equals("월곡")) TOPIS = "월곡(동덕여대)";
        else if (ODSay.equals("어린이대공원")) TOPIS = "어린이대공원(세종대)";
        else if (ODSay.equals("상도")) TOPIS = "상도(중앙대앞)";
        else if (ODSay.equals("신정")) TOPIS = "신정(은행정)";
        else if (ODSay.equals("광나루")) TOPIS = "광나루(장신대)";
        else if (ODSay.equals("새절")) TOPIS = "새절(신사)";
        else if (ODSay.equals("상월곡")) TOPIS = "상월곡(한국과학기술연구원)";
        else if (ODSay.equals("화랑대")) TOPIS = "화랑대(서울여대입구)";
        else if (ODSay.equals("응암")) TOPIS = "응암순환(상선)";
        else if (ODSay.equals("아차산")) TOPIS = "아차산(어린이대공원후문)";
        else if (ODSay.equals("안암")) TOPIS = "안암(고대병원앞)";

        return TOPIS;
    }

    private Double getStationId(String sid) {
        String outCode = sid.substring(6, 10);
        Double result = 0.0;
        if (outCode.equals("2344")) result = 233.2;
        else if (outCode.equals("2343")) result = 233.4;
        else if (outCode.equals("2342")) result = 233.6;
        else if (outCode.equals("2341")) result = 233.8;
        else if (outCode.equals("2114")) result = 210.2;
        else if (outCode.equals("2113")) result = 210.4;
        else if (outCode.equals("2112")) result = 210.6;
        else if (outCode.equals("2111")) result = 210.8;
        else result = Double.parseDouble(outCode.substring(1, 4));

        return result;
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
