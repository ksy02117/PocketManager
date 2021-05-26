package com.example.pocketmanager.ui.timetable;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class EverytimeCrawlingTask extends AsyncTask<String, Void, String> {

    /* 에브리타임 시간표 크롤링
     * https://rul-lu.tistory.com/19
     * https://blog.naver.com/PostView.nhn?blogId=sag120&logNo=222286498723
     * https://seungsulee.tistory.com/24
     */
    // 로그인 정보
    private String everytimeID = "";
    private String everytimePassword = "";

    // 로그인 쿠키
    private Map<String, String> loginCookie;

    // 시간표 ID
    private String timetableID = "";

    // 브라우저
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";

    // 디폴트 생성자
    public EverytimeCrawlingTask() {

    }


    // 에브리타임에서 시간표 가져오기
    protected String doInBackground(String... strings) {
        everytimeID = strings[0];
        everytimePassword = strings[1];
        String result = "";
        try {
            everytimeLogin();
            getTimetableID();
            result = getTimetableHTML();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 크롤링을 위한 에브리타임 로그인
    public boolean everytimeLogin() throws Exception {
        // 전송할 데이터
        Map<String, String> loginData = new HashMap<>();
        loginData.put("userid", everytimeID);
        loginData.put("password", everytimePassword);
        loginData.put("redirect", "/");

        // 에브리타임 로그인(POST)
        Connection.Response login = Jsoup.connect("https://everytime.kr/user/login")
                .userAgent(userAgent)
                .timeout(3000)
                .header("Host","everytime.kr")
                .header("Origin", "https://everytime.kr/")
                .header("Referer", "https://everytime.kr")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .data(loginData)
                .method(Connection.Method.POST)
                .execute();

        // 로그인 성공 후 얻은 쿠키 저장
        loginCookie = login.cookies();

        return true;

    }

    public void getTimetableID() throws IOException {
        //https://api.everytime.kr/find/timetable/table/list/semester
        // 시간표 id
        Map<String, String> yearAndSemester = new HashMap<>();
        yearAndSemester.put("year", "2021");
        yearAndSemester.put("semester", "1");

        // 시간표 정보 받기(POST)
        Connection.Response table = Jsoup.connect("https://api.everytime.kr/find/timetable/table/list/semester")
                .userAgent(userAgent)
                .timeout(3000)
                .header("Host", "api.everytime.kr")
                .header("Origin", "https://everytime.kr")
                .header("Referer", "https://everytime.kr/")
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Content-Length", "20")
                .header("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .data(yearAndSemester)
                .cookies(loginCookie)
                .method(Connection.Method.POST)
                .execute();
        //System.out.println(table.body());
        Document doc = table.parse();
        timetableID = doc.select("table").attr("id").toString();

    }

    private String getTimetableHTML() throws IOException {
        // 시간표 id
        //getTimetableID();
        Map<String, String> timeTableIDmap = new HashMap<>();
        timeTableIDmap.put("id", timetableID);

        // 시간표 정보 받기(POST)
        Connection.Response table = Jsoup.connect("https://api.everytime.kr/find/timetable/table")
                .userAgent(userAgent)
                .timeout(3000)
                .header("Host", "api.everytime.kr")
                .header("Origin", "https://everytime.kr")
                .header("Referer", "https://everytime.kr/")
                .header("Accept", "*/*")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Connection", "keep-alive")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("Content-Length", "11")
                .header("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-site")
                .data(timeTableIDmap)
                .cookies(loginCookie)
                .method(Connection.Method.POST)
                .execute();

        //Document로 전환해 리턴
        return table.parse().toString();
    }

}
