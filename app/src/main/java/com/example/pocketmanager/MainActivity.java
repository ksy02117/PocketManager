package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pocketmanager.network.AirPollutionReceiver;
import com.example.pocketmanager.network.GeoCodingReceiver;
import com.example.pocketmanager.network.WeatherReceiver;
import com.example.pocketmanager.ui.schedule.ScheduleFragment;
import com.example.pocketmanager.ui.timetable.Lecture;
import com.example.pocketmanager.ui.timetable.TimetableManager;
import com.example.pocketmanager.ui.transporation.IncommingTrain;
import com.example.pocketmanager.ui.transporation.PathInfoManager;
import com.example.pocketmanager.ui.transporation.TransportationFragment;
import com.example.pocketmanager.ui.weather.WeatherSelection;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private TextView curDate;
    private FragmentManager fragmentManager;
    private Fragment menu1Fragment;
    private Fragment menu2Fragment;
    private Fragment menu3Fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_schedule:
                            if (menu1Fragment == null) {
                                menu1Fragment = new ScheduleFragment();
                                fragmentManager.beginTransaction().add(R.id.main_frame, menu1Fragment).commit();
                            }
                            else fragmentManager.beginTransaction().show(menu1Fragment).commit();

                            if(menu2Fragment != null) fragmentManager.beginTransaction().hide(menu2Fragment).commit();
                            if(menu3Fragment != null) fragmentManager.beginTransaction().hide(menu3Fragment).commit();
                            setMainText(curDate, "일정");

                            return true;
                        case R.id.menu_transportation:
                            if (menu2Fragment == null) {
                                menu2Fragment = new TransportationFragment();
                                fragmentManager.beginTransaction().add(R.id.main_frame, menu2Fragment).commit();
                            }
                            else fragmentManager.beginTransaction().show(menu2Fragment).commit();

                            if(menu1Fragment != null) fragmentManager.beginTransaction().hide(menu1Fragment).commit();
                            if(menu3Fragment != null) fragmentManager.beginTransaction().hide(menu3Fragment).commit();
                            setMainText(curDate, "교통");

                            return true;
                        case R.id.menu_weather:
                            if (menu3Fragment == null) {
                                menu3Fragment = new WeatherSelection();
                                fragmentManager.beginTransaction().add(R.id.main_frame, menu3Fragment).commit();
                            }
                            else fragmentManager.beginTransaction().show(menu3Fragment).commit();

                            if(menu1Fragment != null) fragmentManager.beginTransaction().hide(menu1Fragment).commit();
                            if(menu2Fragment != null) fragmentManager.beginTransaction().hide(menu2Fragment).commit();
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
        fragmentManager = getSupportFragmentManager();
        menu1Fragment = new ScheduleFragment();

        //Network Receivers
        WeatherReceiver.getInstance(this);
        AirPollutionReceiver.getInstance(this);
        GeoCodingReceiver.getInstance(this);
        GeoCodingReceiver.getCurrentAddress();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,menu1Fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.main_frame,menu1Fragment).commit();

        curDate = (TextView)findViewById(R.id.current_date);
    //test (유진)-------------------------------------------------------------
        /*PathInfoManager p = new PathInfoManager();
        p.setDestination("37.548918, 127.075117");
        p.setOrigin("37.546988, 127.105476");
        p.setSubwayName("광나루(장신대)");
        ArrayList<IncommingTrain> a;
        try {
            a = p.getIncomingTrainInfo();
            for (IncommingTrain i : a){
                i.log();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            p.getShortestPathInfo();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        TimetableManager t = new TimetableManager("jjiny3773","rudgh0607");
        try {
            ArrayList<Lecture> lectures = t.getTimetable();
            for (Lecture l : lectures) l.log();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //---------------------------------------------------------------------

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