package com.example.pocketmanager.schedule;

import com.example.pocketmanager.schedule.EverytimeCrawlingTask;
import com.example.pocketmanager.schedule.Lecture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class TimetableManager {
    // 에브리타임 아이디, 비밀번호
    private String everytimeID = "";
    private String everytimePassword = "";

    // 아이디 비번주는 생성자
    public TimetableManager(String everytimeID , String everytimePassword) {
        this.everytimeID = everytimeID;
        this.everytimePassword = everytimePassword;
    }

    // 디폴트 생성자
    public TimetableManager() {

    }

    // setters
    public void setEverytimeID(String everytimeID) {
        this.everytimeID = everytimeID;
    }

    public void setEverytimePassword(String everytimePassword) {
        this.everytimePassword = everytimePassword;
    }

    public ArrayList<Lecture> getTimetable() throws IOException, ExecutionException, InterruptedException {
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
            lecture.setTime(times.get(i).attr("value").toString());            // "수13:30-16:30"
            lecture.setPlace(places.get(i).attr("value").toString());          // "센B112"

            //lectures에 추가
            lectures.add(lecture);
        }

        return lectures;
    }
}