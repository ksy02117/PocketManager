package com.example.pocketmanager.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.map.LocationDBHelper;
import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.schedule.storage.EventDBHelper;
import com.example.pocketmanager.weather.receiver.AirPollutionReceiver;
import com.example.pocketmanager.map.GeoCodingReceiver;
import com.example.pocketmanager.weather.receiver.DailyWeatherReceiver;
import com.example.pocketmanager.weather.receiver.EventWeatherReceiver;
import com.example.pocketmanager.weather.receiver.HistoricalWeatherReceiver;
import com.example.pocketmanager.weather.receiver.WeatherForecastReceiver;
import com.example.pocketmanager.weather.WeatherData;
import com.example.pocketmanager.home.ui.HomeFragment;
import com.example.pocketmanager.schedule.ui.ScheduleFragment;
import com.example.pocketmanager.map.ui.MapFragment;
import com.example.pocketmanager.transportation.ShortestPath;
import com.example.pocketmanager.weather.ui.WeatherSelection;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView curDate;
    private FragmentManager fragmentManager;
    private Fragment menu1Fragment;
    private Fragment menu2Fragment;
    private Fragment menu3Fragment;
    private Fragment menu4Fragment;

    // 최단 경로
    ShortestPath s = new ShortestPath();

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
        EventWeatherReceiver.getInstance(this);
        GeoCodingReceiver.getInstance(this);

        //DataBases
        DBHelper.getInstance(this);
        //for clearing event every time
        EventDBHelper.clear();
        LocationDBHelper.clear();
        //
        LocationDBHelper.initLocations();
        EventDBHelper.initEvents();


        //weatherData
        LocationData.receiveCurrentLocation();
        WeatherData.receiveWeatherData();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,menu4Fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.main_frame,menu4Fragment).commit();

        curDate = (TextView)findViewById(R.id.current_date);



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

}