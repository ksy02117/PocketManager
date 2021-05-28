package com.example.pocketmanager.schedule.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pocketmanager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class addScheduleActivity extends Activity implements View.OnClickListener {
    private Calendar tmpCal;
    private Button confirm, cancel;
    private EditText eventName, eventDesc;
    private TextView startDate, endDate;
    private String eventNameString, eventDescString;
    private TimePicker startPicker, endPicker;
    private DatePickerDialog.OnDateSetListener startDateListener, endDateListener;
    private int type;
    private int startHour, startMinute, endHour, endMinute;

    // type
    private final int ADD = 0;
    private final int MODIFY = 1;

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
        tmpCal = Calendar.getInstance();
        confirm = (Button) findViewById(R.id.add_confirm);
        cancel = (Button) findViewById(R.id.add_cancel);
        eventName = (EditText) findViewById(R.id.event_name);
        eventDesc = (EditText) findViewById(R.id.event_description);
        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        startPicker = (TimePicker) findViewById(R.id.start_time_picker);
        endPicker = (TimePicker) findViewById(R.id.end_time_picker);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

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
        int type = intent.getIntExtra("type", 0);
        if (type == MODIFY) {
            String name = intent.getStringExtra("eventName");
            String description = intent.getStringExtra("eventDescription");
            long startDt = intent.getLongExtra("startDt", 0);
            long endDt = intent.getLongExtra("endDt", 0);

            eventName.setText(name);
            eventDesc.setText(description);

        }
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
            Toast.makeText(this, "Empty Name", Toast.LENGTH_SHORT).show();
            return;
        }
        eventDescString = eventDesc.getText().toString();

        String[] arrStart = startDate.getText().toString().split("\\.");
        String[] arrEnd = endDate.getText().toString().split("\\.");

        startHour = startPicker.getCurrentHour();
        startMinute = startPicker.getCurrentMinute();
        endHour = endPicker.getCurrentHour();
        endMinute = endPicker.getCurrentMinute();
        tmpCal.set(Integer.parseInt(arrStart[0]), Integer.parseInt(arrStart[1]) - 1, Integer.parseInt(arrStart[2]),
                startHour, startMinute);
        long startDt = tmpCal.getTimeInMillis();

        tmpCal.set(Integer.parseInt(arrEnd[0]), Integer.parseInt(arrEnd[1]) - 1, Integer.parseInt(arrEnd[2]),
                endHour, endMinute);
        long endDt = tmpCal.getTimeInMillis();

        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        intent.putExtra("event_name", eventNameString);
        intent.putExtra("event_description", eventDescString);
        intent.putExtra("start_time", startDt);
        intent.putExtra("end_time", endDt);
        intent.putExtra("latitude", -90.0);
        intent.putExtra("longitude", 0.0);
        setResult(RESULT_OK, intent);

        finish();
    }

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
