package com.example.pocketmanager.ui.transporation;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiCallTask extends AsyncTask<String, Void, String> {
    private String str;    // 받은 xml 이나 json 정보를 result 문자열로 변환할때 사용할 임시 문자열
    private String result; // 결과 문자열


    @Override
    protected String doInBackground(String... strings) {
        try {
            // 전달받은 주소 저장
            URL url = new URL(strings[0]);
            // api 호출
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == conn.HTTP_OK) { // api 호출에 성공
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();

                // 결과물을 buffer에 한줄씩 받아옴
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                    buffer.append("\n");
                }
                // result 문자열에 저장
                result = buffer.toString();

                reader.close();
            }
        } // 예외처리
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }
}
