package com.example.pocketmanager.schedule;

import com.example.pocketmanager.schedule.storage.Lecture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class TimetableManager {
    // 에브리타임 아이디, 비밀번호
    private static String everytimeID = "";
    private static String everytimePassword = "";

    // 아이디 비번주는 생성자
    public TimetableManager(String everytimeID , String everytimePassword) {
        this.everytimeID = everytimeID;
        this.everytimePassword = everytimePassword;
    }

    // 디폴트 생성자
    public TimetableManager() {

    }

    // setters
    public static void setEverytimeID(String everytimeIDs) {
        everytimeID = everytimeIDs;
    }

    public static void setEverytimePassword(String everytimePasswords) {
        everytimePassword = everytimePasswords;
    }

    public static ArrayList<Lecture> getTimetable() throws IOException, ExecutionException, InterruptedException {
        // 시간표 정보를 담은 HTML을 불러옴
        String[] params = {everytimeID, everytimePassword};
        String temp = new EverytimeCrawlingTask().execute(params).get();
        Document timetableHTML = Jsoup.parse(temp);

        // 얻은 HTML에서 name, professor, time, place태그를 가진 라인을 각각 Elements에 저장
        Elements names = timetableHTML.select("name");    //예시! // <name value="오픈소스SW개론" />, ......
        Elements professors = timetableHTML.select("professor"); // <professor value="변정현" />, .....
        Elements times = timetableHTML.select("time");           // <time value="수13:30-16:30" />, .....
        Elements places = timetableHTML.select("place");         // <place value="센B112" />, .....

        // 강의를 추상화한 lecture를 저장할 ArrayList 생성
        ArrayList<Lecture> lectures = new ArrayList<Lecture>();

        for (int i = 0 ; i < names.size(); i++) {
            Lecture lecture = new Lecture();
            // 아래 각각의 데이터들이 lecture에 저장됨 (예시)
            lecture.setName(names.get(i).attr("value").toString());            // "오픈소스SW개론"
            lecture.setProfessor(professors.get(i).attr("value").toString());  // "변정현"
            String tmp = times.get(i).attr("value").toString();
            ArrayList<String> timeArr = new ArrayList<String>();
            if (tmp.length() == 0)
                continue;
            if (tmp.contains(",")) {
                String[] strArr = tmp.split(",");
                Collections.addAll(timeArr, strArr);
            }
            else if (tmp.charAt(1) == '월' || tmp.charAt(1) == '화' || tmp.charAt(1) == '수' || tmp.charAt(1) == '목'|| tmp.charAt(1) == '금') {
                timeArr.add(tmp.charAt(0) + tmp.substring(2));
                timeArr.add(tmp.charAt(1) + tmp.substring(2));
            }
            else timeArr.add(tmp);

            lecture.setTime(timeArr);                                                      // "수13:30-16:30"
            lecture.setPlace(places.get(i).attr("value").toString());          // "센B112"

            //lectures에 추가
            lectures.add(lecture);
        }

        return lectures;
    }
}