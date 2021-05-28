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

public class addEveryTimeActivity extends Activity implements View.OnClickListener {
    private EditText loginID, loginPW;
    private Button loginBT;
    private String id, pw;

    public addEveryTimeActivity() { }
    public static addEveryTimeActivity getInstance() {
        addEveryTimeActivity e = new addEveryTimeActivity();

        return e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.everytime_login);

        //UI 객체생성
        loginID = (EditText) findViewById(R.id.editID);
        loginPW = (EditText) findViewById(R.id.editPassword);
        loginBT = (Button) findViewById(R.id.bt_Login);


        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
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
        id = loginID.getText().toString();
        pw = loginPW.getText().toString();
        if (id.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "Empty ID or Password", Toast.LENGTH_SHORT).show();
            return;
        }

        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        intent.putExtra("everytimeID", id);
        intent.putExtra("everytimePW", pw);
        setResult(RESULT_OK, intent);

        finish();
    }
}
