package com.example.pocketmanager.schedule.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pocketmanager.R;
import com.example.pocketmanager.general.Time;
import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.schedule.storage.Event;
import com.example.pocketmanager.schedule.storage.SubEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class addScheduleActivity extends Activity implements View.OnClickListener {
    private Calendar tmpCal;
    private Button confirm, cancel;
    private EditText eventName, eventDesc;
    private ImageView addLocation;
    private TextView startDate, endDate;
    private String eventNameString, eventDescString;
    private DatePicker startDatePicker, endDatePicker;
    private TimePicker startPicker, endPicker;
    private LocationData location;
    private Time startTime, endTime;
    private Event parentEvent, modifyEvent;
    private CheckBox outdoorCheck;
    private DatePickerDialog.OnDateSetListener startDateListener, endDateListener;
    private boolean isOutdoor = false;
    private int eventType;  // 0: Event | 1: SubEvent
    private int type;       // 0: add   | 1: modify
    private int startHour, startMinute, endHour, endMinute;

    // type
    private final int ADD = 0;
    private final int MODIFY = 1;
    private final int PARENT = 0;
    private final int SUB = 1;

    public addScheduleActivity() { }
    public static addScheduleActivity getInstance() {
        addScheduleActivity e = new addScheduleActivity();

        return e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add);

        //UI 객체생성
        tmpCal = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
        confirm = (Button) findViewById(R.id.add_confirm);
        cancel = (Button) findViewById(R.id.add_cancel);
        eventName = (EditText) findViewById(R.id.event_name);
        eventDesc = (EditText) findViewById(R.id.event_description);
        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        startPicker = (TimePicker) findViewById(R.id.start_time_picker);
        endPicker = (TimePicker) findViewById(R.id.end_time_picker);
        addLocation = (ImageView) findViewById(R.id.add_location);
        outdoorCheck = (CheckBox) findViewById(R.id.add_outdoor_check);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), addLocationActivity.class);
                intent.putExtra("test", "hello world");
                startActivityForResult(intent, 1);
            }
        });


        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tmpCal.set(year, month, dayOfMonth);
                startDate.setText(sd.format(tmpCal.getTime()));
                endDate.setText(sd.format(tmpCal.getTime()));
            }
        };
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tmpCal.set(year, month, dayOfMonth);
                endDate.setText(sd.format(tmpCal.getTime()));
            }
        };

        startDate.setText(sd.format(tmpCal.getTime()));
        endDate.setText(sd.format(tmpCal.getTime()));

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        eventName.setFocusableInTouchMode(true);
        eventName.requestFocus();

        //데이터 가져오기
        Intent intent = getIntent();
        eventType = intent.getIntExtra("eventType", 0);
        type = intent.getIntExtra("type", 0);

        if (eventType == SUB)
            parentEvent = (Event) intent.getSerializableExtra("parentEvent");

        if (type == MODIFY) {
            modifyEvent = (Event) intent.getSerializableExtra("event");

            eventName.setText(modifyEvent.getEventName());
            eventDesc.setText(modifyEvent.getDescription());
            Time st = modifyEvent.getStartTime();
            Time et = modifyEvent.getEndTime();
            startDateListener.onDateSet(startDatePicker, st.getYear(), st.getMonth() - 1, st.getDay());
            endDateListener.onDateSet(endDatePicker, et.getYear(), et.getMonth() - 1, et.getDay());
            startPicker.setCurrentHour(st.getHour());
            startPicker.setCurrentMinute(st.getMin());
            endPicker.setCurrentHour(et.getHour());
            endPicker.setCurrentMinute(et.getMin());
        }
    }

    private Event modifyEvent() {
        Event.removeEvent(modifyEvent);
        return addParentEvent();
    }

    private Event addParentEvent() {
        return Event.createEvent(eventNameString, startTime, endTime, null, isOutdoor, eventDescString, Event.PRIORITY_MEDIUM);
    }

    private boolean addSubEvent(Event parentEvent) {
        // 부모 이벤트보다 시작 시간이 이르면
        if (startTime.compareTo(parentEvent.getStartTime()) < 1) {
            Toast.makeText(this, "시작 시간이 너무 이릅니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 부모 이벤트보다 종료 시간이 늦으면
        if (endTime.compareTo(parentEvent.getEndTime()) > 1) {
            Toast.makeText(this, "종료 시간이 너무 늦습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 서브 이벤트 생성
        parentEvent = Event.findEventByID(parentEvent.getID());
        SubEvent.createSubEvent(parentEvent, eventNameString, startTime, endTime, null, isOutdoor, eventDescString, Event.PRIORITY_MEDIUM);
        return true;
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;

        // 취소
        if (b.getText().equals("취소")) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        // 빈 일정 이름 경고
        eventNameString = eventName.getText().toString();
        if (eventNameString.isEmpty()) {
            Toast.makeText(this, "제목이 비어있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        eventDescString = eventDesc.getText().toString();

        String[] arrStart = startDate.getText().toString().split("\\.");
        String[] arrEnd = endDate.getText().toString().split("\\.");

        startHour = startPicker.getCurrentHour();
        startMinute = startPicker.getCurrentMinute();
        endHour = endPicker.getCurrentHour();
        endMinute = endPicker.getCurrentMinute();
        startTime = new Time(Integer.parseInt(arrStart[0]), Integer.parseInt(arrStart[1]), Integer.parseInt(arrStart[2]),
                startHour, startMinute, 0);
        endTime = new Time(Integer.parseInt(arrEnd[0]), Integer.parseInt(arrEnd[1]), Integer.parseInt(arrEnd[2]),
                endHour, endMinute, 0);
        isOutdoor = outdoorCheck.isChecked();

        if (startTime.compareTo(endTime) >= 0) {
            Toast.makeText(this, "종료 시간이 시작 시간보다 빠릅니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type == MODIFY) {
            Event modified = modifyEvent();

            Intent intent = new Intent();
            intent.putExtra("event", modified);

            setResult(1, intent);
            finish();
            return;
        }

        if (eventType == SUB) {
            if (!addSubEvent(parentEvent))
                return;
        }
        else
            addParentEvent();

        setResult(RESULT_OK);
        finish();
/*
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        intent.putExtra("event_name", eventNameString);
        intent.putExtra("event_description", eventDescString);
        intent.putExtra("start_time", startTime);
        intent.putExtra("end_time", endTime);
        intent.putExtra("latitude", -90.0);
        intent.putExtra("longitude", 0.0);

 */
    }

    // DATE PICKER
    public void onClickStartDialog(View view) {
        String[] arr = startDate.getText().toString().split("\\.");

        DatePickerDialog dialog = new DatePickerDialog(this, startDateListener,
                Integer.parseInt(arr[0]), Integer.parseInt(arr[1]) - 1, Integer.parseInt(arr[2]));
        dialog.show();
    }

    public void onClickEndDialog(View view) {
        String[] arr = endDate.getText().toString().split("\\.");

        DatePickerDialog dialog = new DatePickerDialog(this, endDateListener,
                Integer.parseInt(arr[0]), Integer.parseInt(arr[1]) - 1, Integer.parseInt(arr[2]));
        dialog.show();
    }
}
