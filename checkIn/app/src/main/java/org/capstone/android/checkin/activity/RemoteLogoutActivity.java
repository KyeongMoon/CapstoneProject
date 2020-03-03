package org.capstone.android.checkin.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.capstone.android.checkin.R;
import org.capstone.android.checkin.data.JSONData;
import org.capstone.android.checkin.http.NetworkTask;

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
        String url = "http://18.218.11.150:8080/checkIN/send";

        JSONData accountData = new JSONData("user_id", "user_pw", null, null);



        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, accountData);
        AsyncTask<Void,Void, String> a = networkTask.execute();
        try {
            logoutTextView.setText(a.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
