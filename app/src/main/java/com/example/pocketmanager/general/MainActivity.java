package com.example.pocketmanager.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.schedule.LocationData;
import com.example.pocketmanager.schedule.alarm.AlarmReceiver;
import com.example.pocketmanager.weather.receiver.AirPollutionReceiver;
import com.example.pocketmanager.map.GeoCodingReceiver;
import com.example.pocketmanager.weather.receiver.DailyWeatherReceiver;
import com.example.pocketmanager.weather.receiver.HistoricalWeatherReceiver;
import com.example.pocketmanager.weather.receiver.WeatherForecastReceiver;
import com.example.pocketmanager.weather.WeatherData;
import com.example.pocketmanager.home.ui.HomeFragment;
import com.example.pocketmanager.schedule.ui.ScheduleFragment;
import com.example.pocketmanager.map.ui.MapFragment;
import com.example.pocketmanager.transportation.IncomingTrain;
import com.example.pocketmanager.transportation.PathInfoManager;
import com.example.pocketmanager.transportation.ShortestPath;
import com.example.pocketmanager.weather.ui.WeatherSelection;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private TextView curDate;
    private FragmentManager fragmentManager;
    private Fragment menu1Fragment;
    private Fragment menu2Fragment;
    private Fragment menu3Fragment;
    private Fragment menu4Fragment;
    ShortestPath s = new ShortestPath();
    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            if (menu4Fragment == null) {
                                menu4Fragment = new HomeFragment();
                                fragmentManager.beginTransaction().add(R.id.main_frame, menu4Fragment).commit();
                            }
                            else fragmentManager.beginTransaction().show(menu4Fragment).commit();

                            if(menu1Fragment != null) fragmentManager.beginTransaction().hide(menu1Fragment).commit();
                            if(menu2Fragment != null) fragmentManager.beginTransaction().hide(menu2Fragment).commit();
                            if(menu3Fragment != null) fragmentManager.beginTransaction().hide(menu3Fragment).commit();
                            setMainText(curDate, "홈");

                            return true;
                        case R.id.menu_schedule:
                            if (menu1Fragment == null) {
                                menu1Fragment = new ScheduleFragment();
                                fragmentManager.beginTransaction().add(R.id.main_frame, menu1Fragment).commit();
                            }
                            else fragmentManager.beginTransaction().show(menu1Fragment).commit();

                            if(menu2Fragment != null) fragmentManager.beginTransaction().hide(menu2Fragment).commit();
                            if(menu3Fragment != null) fragmentManager.beginTransaction().hide(menu3Fragment).commit();
                            if(menu4Fragment != null) fragmentManager.beginTransaction().hide(menu4Fragment).commit();
                            setMainText(curDate, "일정");

                            return true;
                        case R.id.menu_weather:
                            if (menu3Fragment == null) {
                                menu3Fragment = new WeatherSelection();
                                fragmentManager.beginTransaction().add(R.id.main_frame, menu3Fragment).commit();
                            }
                            else fragmentManager.beginTransaction().show(menu3Fragment).commit();

                            if(menu1Fragment != null) fragmentManager.beginTransaction().hide(menu1Fragment).commit();
                            if(menu2Fragment != null) fragmentManager.beginTransaction().hide(menu2Fragment).commit();
                            if(menu4Fragment != null) fragmentManager.beginTransaction().hide(menu4Fragment).commit();
                            setMainText(curDate, "날씨");

                            return true;
                        case R.id.menu_map:
                            if (menu2Fragment == null) {
                                menu2Fragment = new MapFragment();
                                fragmentManager.beginTransaction().add(R.id.main_frame, menu2Fragment).commit();
                            }
                            else fragmentManager.beginTransaction().show(menu2Fragment).commit();
                            // MapFragment에 ShortestPath 객체 전달
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ShortestPath", s);
                            menu2Fragment.setArguments(bundle);

                            if(menu1Fragment != null) fragmentManager.beginTransaction().hide(menu1Fragment).commit();
                            if(menu3Fragment != null) fragmentManager.beginTransaction().hide(menu3Fragment).commit();
                            if(menu4Fragment != null) fragmentManager.beginTransaction().hide(menu4Fragment).commit();
                            setMainText(curDate, "지도");

                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        //Fragments
        fragmentManager = getSupportFragmentManager();
        menu4Fragment = new HomeFragment();

        //Network Receivers
        HistoricalWeatherReceiver.getInstance(this);
        WeatherForecastReceiver.getInstance(this);
        DailyWeatherReceiver.getInstance(this);
        AirPollutionReceiver.getInstance(this);
        GeoCodingReceiver.getInstance(this);


        //weatherData
        LocationData.receiveCurrentLocation();
        WeatherData.receiveWeatherData();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,menu4Fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.main_frame,menu4Fragment).commit();

        curDate = (TextView)findViewById(R.id.current_date);

        // test (유진)-----------------------
        PathInfoManager p = new PathInfoManager();

        p.setDestination("37.550266, 127.073351");
        //p.setOrigin(LocationData.getCurrentLocation().getLatitude() + ", " + LocationData.getCurrentLocation().getLongitude());
        //p.setOrigin("37.546988, 127.105476");
        //p.setOrigin("37.466600, 126.824800");
        p.setOrigin("37.509593, 126.773195");
        //p.setSubwayName("광나루(장신대)");


        try {
            s = p.getShortestPathInfo();
            ArrayList<IncomingTrain> t = p.getIncomingTrainInfo();
            for (IncomingTrain a : t){
                a.log();
            }




        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        mCalender = new GregorianCalendar();


        //setContentView(R.layout.activity_main);
        //setAlarm();
        //----------------------------

    }

    public void setDate(TextView view) {
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        String date = formatter.format(today);
        view.setText(date);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    LocationData.receiveCurrentLocation();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void setMainText(TextView view, String str) {
        view.setText(str);
    }

    public void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, 0);

        String from = "2021-05-28 23:20:20"; //임의로 날짜와 시간을 지정

        //날짜 포맷을 바꿔주는 소스코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);


    }



}