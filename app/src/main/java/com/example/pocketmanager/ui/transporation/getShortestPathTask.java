package com.example.pocketmanager.ui.transporation;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class getShortestPathTask extends AsyncTask<String, Void, String> {
    private String str, result;


    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=37.546988, 127.105476&destination=37.548918, 127.075117&mode=transit&departure_time=now&language=ko&key=AIzaSyD8wydCmxm4OVhOH1Yedy39C-o_ypgk48I");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                    buffer.append("\n");
                }
                result = buffer.toString();

                reader.close();
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }
}

