package com.example.pocketmanager.ui.transporation;

import android.util.Log;

import java.text.SimpleDateFormat;

public class IncommingTrain {
    private String direction;  // 열차 방향(행선지)
    private String name;       // 열차 이름
    private String arriveTime; // 열차 도착 시간

    public void log() {
        Log.d("name", name);
        Log.d("direction", direction);
        Log.d("arriveTime", arriveTime);
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


}
