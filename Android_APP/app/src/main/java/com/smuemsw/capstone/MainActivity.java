package com.smuemsw.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //원격 로그아웃
        Button remoteLogoutButton = findViewById(R.id.moveRemoteLogoutButton);

        //OTP 생성
        Button createOTPButton = findViewById(R.id.moveCreateOTPButton);

        //1회용 로그인 번호 생성
        Button createLoginNumberButton = findViewById(R.id.moveCreateLoginNumberButton);

        //접속 로그 확인
        Button accessLogButton = findViewById(R.id.moveAccessLogButton);


    }

    public void moveRemoteLogoutActivity(View view) {
        Intent intent = new Intent(MainActivity.this, RemoteLogoutActivity.class);
        startActivity(intent);
    }

    public void moveCreateOTPActivity(View view) {
        Intent intent = new Intent(MainActivity.this, CreateOTPActivity.class);
        startActivity(intent);
    }

    public void moveCreateLoginNumberActivity(View view) {
        Intent intent = new Intent(MainActivity.this, CreateLoginNumberActivity.class);
        startActivity(intent);
    }

    public void moveAccessLogActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AccessLogActivity.class);
        startActivity(intent);
    }
}
