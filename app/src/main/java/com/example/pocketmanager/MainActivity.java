package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketmanager.network.AirPollutionReceiver;
import com.example.pocketmanager.network.GeoCodingReceiver;
import com.example.pocketmanager.network.WeatherReceiver;
import com.example.pocketmanager.ui.home.HomeFragment;
import com.example.pocketmanager.ui.schedule.ScheduleFragment;
import com.example.pocketmanager.ui.map.MapFragment;
import com.example.pocketmanager.ui.transporation.IncomingTrain;
import com.example.pocketmanager.ui.transporation.PathInfoManager;
import com.example.pocketmanager.ui.transporation.ShortestPath;
import com.example.pocketmanager.ui.weather.WeatherSelection;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private Fragment menu4Fragment;
    ShortestPath s;

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
                            bundle.putSerializable("Obj", s);
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
        WeatherReceiver.getInstance(this);
        AirPollutionReceiver.getInstance(this);
        GeoCodingReceiver.getInstance(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,menu4Fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.main_frame,menu4Fragment).commit();

        curDate = (TextView)findViewById(R.id.current_date);

        // test (유진)-----------------------

        PathInfoManager p = new PathInfoManager();

        p.setDestination("37.548918, 127.075117");
        p.setOrigin("37.546988, 127.105476");
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


        //getHashKey();

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
                    GeoCodingReceiver.getCurrentAddress();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
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

    /*private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }*/


}