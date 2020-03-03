package org.capstone.android.checkin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.capstone.android.checkin.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //최초 로그인인 경우 지문 등록을 물어본다.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        if(!preferences.getBoolean("fingerPrint", false)){
            editor.putBoolean("fingerPrint", true);
            editor.commit();

            startActivity(new Intent(MainActivity.this, FingerTestActivity.class));
        }


//        //원격 로그아웃
//        ImageView remoteLogoutButton = findViewById(R.id.moveRemoteLogoutButton);
//
//        //OTP 생성
//        ImageButton createOTPButton = findViewById(R.id.moveCreateOTPButton);
//
//        //1회용 로그인 번호 생성
//        ImageView createLoginNumberButton = findViewById(R.id.moveCreateLoginNumberButton);
//
//        //접속 로그 확인
//        ImageButton accessLogButton = findViewById(R.id.moveAccessLogButton);


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

    public void moveFingerTestActivity(View view) {
        startActivity(new Intent(MainActivity.this,  FingerTestActivity.class));
    }
}
