package com.example.pocketmanager.schedule.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.example.pocketmanager.general.MainActivity;
import com.example.pocketmanager.weather.DailyWeatherData;
import com.example.pocketmanager.weather.WeatherData;
import com.example.pocketmanager.general.Time;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class Alarm {// 알람을 생성하기 위한 클래스 입니다.
    private static AlarmManager alarmManager;
    private GregorianCalendar mCalender;
    private NotificationManager notificationManager;
    private static Context context;

    public Alarm(Context c) { //생성자
        context = c;
        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();
    }

    public static void setAlarm(String time, String name, String describe) {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(context, AlarmReceiver.class);
        receiverIntent.putExtra("describe", describe);
        receiverIntent.putExtra("name", name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, receiverIntent, 0);

        //날짜 포맷을 바꿔주는 소스코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        Calendar currentTime = Calendar.getInstance();
        if (calendar.getTimeInMillis() < currentTime.getTimeInMillis())
            return;

        // 알람 생성함
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
    }

    public static void departAlarm(String time) {
        String name = "";
        String description = "";
        boolean airPollution = false;
        boolean highTempRange = false;
        boolean rainFlag = false;

        List<WeatherData> list = WeatherData.getTodayWeather();
        Iterator<WeatherData> it = list.iterator();

        while (it.hasNext()){
            WeatherData weather = it.next();
            if (weather.getPm2_5() > 35)
                airPollution = true;
            if (weather.getPm10() > 80)
                airPollution = true;
            if (weather.getPop() > 0)
                rainFlag = true;
        }

        DailyWeatherData dw= WeatherData.dailyWeatherData.get(0);
        double tempRange = dw.getMax_temp() - dw.getMin_temp();

        if (tempRange > 15)
            highTempRange = true;

        if (airPollution || highTempRange || rainFlag)
            name = "날씨 주의!";
        else
            name = "출발합시다";

        if (airPollution)
            description += "미세먼지농도가 높습니다. 마스크는 필수!\n";
        if (highTempRange)
            description += "일교차가 높아요. 것옷 챙겨가세요!\n";
        if (rainFlag)
            description += "오늘 비가 온다네요, 우산 챙겨가세요\n";

        if (description.equals(""))
            description = "오늘은 날씨가 평화로워요.";

        setAlarm(time, name, description);
    }

}
