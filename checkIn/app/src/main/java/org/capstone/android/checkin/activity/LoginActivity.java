package org.capstone.android.checkin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.capstone.android.checkin.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private static final String id_test = "qw";
    private static final String pw_test = "12";

    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        Toast.makeText(this, "why", Toast.LENGTH_SHORT);
        String a = preferences.getString("testId", "fail");
        _loginButton.setText(a);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        already();
    }

    //TODO : 자동 로그인 구현, id와 pw를 받아 서버에 접속 요청하기
    public void already(){
        String id = preferences.getString("id", "user_id");
        String pw = preferences.getString("pw","user_pw");
    }

    public void login() {


        editor.putString("testId", _emailText.getText().toString());
        editor.commit();


        Log.d(TAG, "Login");

        //페이지 넘기고 수정
//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        finish();


        // TODO : 이메일, 비밀번호 형식을 잘못 입력한 경우 오류메세지를 출력할지.
        //아이디 비밀번호 형식이 유효하지 않다면 Toast 를 띄우고 종료
//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }

        _loginButton.setEnabled(false);


        //R.style.AppTheme_Dark_Dialog
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        //3초동안 progressbar 돌리기,그리고 현재화면 종료 TODO: 3초뒤 프로세스가 마무리 되는것이기 때문에 이전에 WAS와 통신을 하고 결과 bit만 아래에 넘겨줄 것.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(email.equals(id_test) && password.equals(pw_test))
                            onLoginSuccess();
                        else
                            onLoginFailed();


                        // On complete call either onLoginSuccess or onLoginFailed
                        //onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void moveFingerTestActivity(View view) {
        startActivity(new Intent(LoginActivity.this, FingerTestActivity.class));
    }
}