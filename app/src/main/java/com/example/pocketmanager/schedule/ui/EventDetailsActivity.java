package com.example.pocketmanager.schedule.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.schedule.storage.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetailsActivity extends Activity implements View.OnClickListener {
    private Event currentEvent;
    private TextView eventName, eventDesc, eventStartDate, eventEndDate, eventStartTime, eventEndTime;
    private String name, description;
    private long startDt, endDt;

    private ImageView addSub;
    private Button cancel, modify, delete;
    private String id, pw;

    public EventDetailsActivity() { }
    public static EventDetailsActivity getInstance() {
        EventDetailsActivity e = new EventDetailsActivity();

        return e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_description);

        //UI 객체생성
        eventName = (TextView) findViewById(R.id.detail_name);
        eventDesc = (TextView) findViewById(R.id.detail_desc);
        eventStartDate = (TextView) findViewById(R.id.detail_start_date);
        eventEndDate = (TextView) findViewById(R.id.detail_end_date);
        eventStartTime = (TextView) findViewById(R.id.detail_start_time);
        eventEndTime = (TextView) findViewById(R.id.detail_end_time);
        addSub = (ImageView) findViewById(R.id.detail_add_sub);
        cancel = (Button) findViewById(R.id.detail_cancel);
        modify = (Button) findViewById(R.id.detail_modify);
        delete = (Button) findViewById(R.id.detail_delete);

        //데이터 가져오기
        Intent intent = getIntent();
        name = intent.getStringExtra("eventName");
        description = intent.getStringExtra("eventDescription");
        startDt = intent.getLongExtra("startTime", 0);
        endDt = intent.getLongExtra("endTime", 0);
        Time startTime = new Time();
        Time endTime = new Time();
        SimpleDateFormat s = new SimpleDateFormat("HH:mm", Locale.getDefault());

        startTime.setDt(startDt);
        endTime.setDt(endDt);

        eventName.setText(name);
        eventDesc.setText(description);
        eventStartDate.setText(startTime.getMonth() + "월 " + startTime.getDay() + "일");
        if (startTime.getMonth() == endTime.getMonth() && startTime.getDay() == endTime.getDay())
            eventEndDate.setVisibility(View.GONE);
        else
            eventEndDate.setText(endTime.getMonth() + "월 " + endTime.getDay() + "일");
        eventStartTime.setText(s.format(new Date(startTime.getDt() * 1000)));
        eventEndTime.setText(s.format(new Date(endTime.getDt() * 1000)));

        cancel.setOnClickListener(this);
        modify.setOnClickListener(this);
        delete.setOnClickListener(this);

        addSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), addScheduleActivity.class);
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;

        // cancel
        if (b.getText().equals("취소")) {
            setResult(RESULT_CANCELED);
            finish();
        }
        // modify
        else if (b.getText().equals("수정")) {
            Intent intent = new Intent(getApplicationContext(), addScheduleActivity.class);
            intent.putExtra("type", 1);
            intent.putExtra("eventName", name);
            intent.putExtra("eventDescription", description);
            intent.putExtra("startDt", startDt);
            intent.putExtra("endDt", endDt);
            startActivityForResult(intent, 1);
        }
        // delete
        else {
            show();
        }
    }
    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("이걸 삭제한다고?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // yes
                        //Event.removeEvent();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // no
                    }
                });
        builder.show();
    }
}
