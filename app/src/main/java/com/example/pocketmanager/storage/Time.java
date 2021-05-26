package com.example.pocketmanager.storage;

import java.util.Calendar;
import java.util.TimeZone;

public class Time implements Comparable<Time>{

    private long dt;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private int sec;

    public long getDt() { return dt; }
    public void setDt(long dt) {
        this.dt = dt;
        setTime(dt);
    }
    private void setTime(long dt) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
        cal.setTimeInMillis(dt * 1000L);

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);
        sec = cal.get(Calendar.SECOND);
    }


    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
    public int getHour() { return hour; }
    public int getMin() { return min; }
    public int getSec() { return sec; }

    public static long getCurrentDt() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    @Override
    public int compareTo(Time o) {
        if (dt == o.dt)
            return 0;
        else if (dt > o.dt)
            return 1;
        else
            return -1;
    }

    public static boolean isCurrentDay(Time t, Calendar c) {
        int year, month, day;

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        return t.getYear() == year && t.getMonth() == month && t.getDay() == day;
    }
}
