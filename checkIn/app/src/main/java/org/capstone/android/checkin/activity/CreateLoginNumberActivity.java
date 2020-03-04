package org.capstone.android.checkin.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.capstone.android.checkin.R;

public class CreateLoginNumberActivity extends AppCompatActivity {

    TextView LoginNumberTextView;
    TextView LoginNumberProgressbarSec;
    ProgressBar LoginNumberProgressBar;
    CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login_number);

        LoginNumberTextView = findViewById(R.id.createLoginNumberTextView);
        LoginNumberProgressbarSec = findViewById(R.id.LoginNumberProgressbarSec);
        LoginNumberProgressBar = findViewById(R.id.LoginNumberProgressbar);


        // TODO : 서버와 최초 연결

        mCountDownTimer = new CountDownTimer(1000 * 60 * 60, 1000) {
            int i = 61;

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);

                if (i == 0 || i == 61) {
                    //TODO: 서버에서 수신한 데이터를 tesview에 반영

                    i = 60;
                }
                LoginNumberProgressBar.setProgress(i);
                LoginNumberProgressbarSec.setText(i + " 초");
                i--;
            }

            @Override
            public void onFinish() {
                //nothing
            }
        };
        mCountDownTimer.start();
    }

    public void FinishActivity(View view) {
        finish();
    }
}
