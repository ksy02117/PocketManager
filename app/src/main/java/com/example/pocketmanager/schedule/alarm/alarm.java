package com.example.pocketmanager.schedule.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.pocketmanager.general.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class alarm {
    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;
    private NotificationManager notificationManager;
    private Context context;

    public alarm(Context c) {
        context = c;
        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();
    }
    public void setAlarm(String time, String name, String describe) {
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

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
    }
}
