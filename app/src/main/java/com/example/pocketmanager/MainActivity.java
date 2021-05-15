package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pocketmanager.network.AirPollutionReceiver;
import com.example.pocketmanager.network.WeatherReceiver;
import com.example.pocketmanager.storage.WeatherData;
import com.example.pocketmanager.ui.schedule.ScheduleFragment;
import com.example.pocketmanager.ui.transporation.TransportationFragment;
import com.example.pocketmanager.ui.weather.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView curDate;
    private Fragment menu1Fragment;
    private Fragment menu2Fragment;
    private Fragment menu3Fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_schedule:
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, menu1Fragment).commit();
                            setMainText(curDate, "일정");
                            return true;
                        case R.id.menu_transportation:
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, menu2Fragment).commit();
                            setMainText(curDate, "교통");
                            return true;
                        case R.id.menu_weather:
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, menu3Fragment).commit();
                            setMainText(curDate, "날씨");
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

        //Fragments
        menu1Fragment = new ScheduleFragment();
        menu2Fragment = new TransportationFragment();
        menu3Fragment = new WeatherFragment();

        //Network Receivers
        WeatherReceiver.getInstance(this);
        AirPollutionReceiver.getInstance(this);



        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,menu1Fragment).commit();

        curDate = (TextView)findViewById(R.id.current_date);
    }

    public void setDate(TextView view) {
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        String date = formatter.format(today);
        view.setText(date);
    }

    public void setMainText(TextView view, String str) {
        view.setText(str);
    }
}