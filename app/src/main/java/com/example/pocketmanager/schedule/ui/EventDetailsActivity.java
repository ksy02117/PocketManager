package com.example.pocketmanager.schedule.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.schedule.storage.Event;
import com.example.pocketmanager.schedule.storage.SubEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetailsActivity extends Activity implements View.OnClickListener {
    private Event currentEvent;
    private TextView eventName, eventDesc, eventStartDate, eventEndDate, eventStartTime, eventEndTime, locationText;
    private String name, description;
    private Time startTime, endTime;
    private LocationData location;
    private SimpleDateFormat s;
    private long startDt, endDt;
    private boolean isOutdoor;

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
        locationText = (TextView) findViewById(R.id.detail_location);
        addSub = (ImageView) findViewById(R.id.detail_add_sub);
        cancel = (Button) findViewById(R.id.detail_cancel);
        modify = (Button) findViewById(R.id.detail_modify);
        delete = (Button) findViewById(R.id.detail_delete);


        //데이터 가져오기
        Intent intent = getIntent();
        currentEvent = (Event) intent.getSerializableExtra("event");
        name = currentEvent.getEventName();
        description = currentEvent.getDescription();
        startDt = currentEvent.getStartTime().getDt();
        endDt = currentEvent.getEndTime().getDt();
        startTime = new Time();
        endTime = new Time();
        location = currentEvent.getLocation();
        s = new SimpleDateFormat("HH:mm", Locale.getDefault());

        setTexts();

        cancel.setOnClickListener(this);
        modify.setOnClickListener(this);
        delete.setOnClickListener(this);

        // add SubEvent
        addSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), addScheduleActivity.class);
                intent.putExtra("eventType", 1);
                intent.putExtra("parentEvent", currentEvent);
                startActivityForResult(intent, 2);
            }
        });
    }

    private void setTexts() {
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
        if (location == null)
            locationText.setText("지정 없음");
        else
            locationText.setText(location.getName());
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
            intent.putExtra("event", currentEvent);
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
        builder.setMessage("일정을 삭제할까요?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Event.removeEvent(currentEvent);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode==1 && data != null){
            if (resultCode == 1) {

                currentEvent = (Event) data.getSerializableExtra("event");
                name = currentEvent.getEventName();
                description = currentEvent.getDescription();
                startDt = currentEvent.getStartTime().getDt();
                endDt = currentEvent.getEndTime().getDt();
                location = currentEvent.getLocation();

                Log.d("hate_name", "" + name);
                Log.d("hate_description", "" + description);
                Log.d("hate_startdt", "" + startDt);
                Log.d("hate_enddt", "" + endDt);

                setTexts();
            }
        }
        if (requestCode == 2 && data != null) {
            delete.setActivated(false);

            String dataName = data.getStringExtra("eventName");
            String dataDescription = data.getStringExtra("eventDescription");
            Time startTime = (Time) data.getSerializableExtra("startTime");
            Time endTime = (Time) data.getSerializableExtra("endTime");

            SubEvent.createSubEvent(currentEvent, dataName, startTime, endTime, null, false, dataDescription, Event.PRIORITY_MEDIUM);

            setResult(RESULT_OK);
            finish();
        }
    }
}
