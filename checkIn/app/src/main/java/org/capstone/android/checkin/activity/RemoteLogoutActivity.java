package org.capstone.android.checkin.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.capstone.android.checkin.R;
import org.capstone.android.checkin.data.JSONData;
import org.capstone.android.checkin.data.LoginJSONData;
import org.capstone.android.checkin.http.NetworkTask;

import java.net.URL;
import java.util.concurrent.ExecutionException;

public class RemoteLogoutActivity extends AppCompatActivity {

    TextView logoutTextView;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_logout);

        logoutTextView = (TextView) findViewById(R.id.remoteLogoutTextView);
        logoutButton = (Button) findViewById(R.id.remoteLogoutButton);

        //URL 설정.
        String url1 = "http://18.218.11.150:8080/checkIN/signIn";

        //TODO : 원격 로그아웃 통신 구성 및 layout 수정

//        JSONData accountData = new JSONData("abcd", "user_pw", null, null);
        LoginJSONData accountData = new LoginJSONData("abcd@naver.com", "1234", "1234567");

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url1, accountData);
        AsyncTask<Void,Void, String> a = networkTask.execute();
        try {
            logoutTextView.setText(a.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logoutTextView.setTextSize(10);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //URL 설정.
                String url2 = "http://18.218.11.150:8080/checkIN/signOut";

                //TODO : 원격 로그아웃 통신 구성 및 layout 수정

                //JSONData accountData = new JSONData("abcd", "user_pw", null, null);
                LoginJSONData accountData2 = new LoginJSONData("abcd@naver.com", "1234", "123");

                // AsyncTask를 통해 HttpURLConnection 수행.
                NetworkTask networkTask2 = new NetworkTask(url2, accountData2);
                AsyncTask<Void,Void, String> a2 = networkTask2.execute();
                try {
                    logoutTextView.setText(a2.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void FinishActivity(View view) {
        finish();
    }
}
