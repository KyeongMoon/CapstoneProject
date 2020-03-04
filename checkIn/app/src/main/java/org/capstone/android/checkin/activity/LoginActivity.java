package org.capstone.android.checkin.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import org.capstone.android.checkin.R;

import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    //TODO : 임시 아이디와 비밀번호 , 보안성을 가진 sharedpreference사용하기
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

        getUUID();
        already();

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    //TODO : 첫 로그인 이후 항상 자동 로그인 구현, 로컬에 있는 정보를 받아 id와 pw를 받아 서버에 접속 요청하기
    public void already() {

        String id = preferences.getString("id", "null");
        String pw = preferences.getString("pw", "null");
        Boolean useFingerPrint = preferences.getBoolean("useFingerPrint", false);


        //TODO : Dialog빌더로 만들까 말까
        //지문등록을 했고, 로그인을 했을 경우 2차인증을 실시한다.
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.e("Finger", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("Finger", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("Finger", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("Finger", "The user hasn't associated any biometric credentials " +
                        "with their account.");
                break;
        }


        // 자동로그인 상태에서 지문등록한 경우, 지문으로 자동로그인
        if (!id.equals("null") && !pw.equals("null") && useFingerPrint) {
            FingerBioFactory a = new FingerBioFactory(this, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    onLoginSuccess();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            });
            a.authenticate();
        }

    }

    public class FingerBioFactory {
        private AppCompatActivity activity;
        private BiometricPrompt.AuthenticationCallback callback;
        private BiometricPrompt myBiometricPrompt;
        private BiometricPrompt.PromptInfo biPromptInfo;

        public FingerBioFactory(AppCompatActivity activity, BiometricPrompt.AuthenticationCallback callback) {
            this.activity = activity;
            this.callback = callback;
            setting();
        }

        private void setting() {
            Executor newExecutor = Executors.newSingleThreadExecutor();

            biPromptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("지문으로 로그인하기")
                    .setDescription("지문을 입력해주세요")
                    .setNegativeButtonText("취소")
                    .build();

            myBiometricPrompt = new BiometricPrompt(activity, newExecutor, callback);
        }

        public void authenticate() {
            myBiometricPrompt.authenticate(biPromptInfo);
        }
    }

    public void login() {
        Log.d(TAG, "Login");

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
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();



        // TODO: Implement your own authentication logic here.
        int loginBit = 0;

        if (email.equals(id_test) && password.equals(pw_test)) {
            editor.putString("id", email);
            editor.putString("pw", password);
            editor.commit();
            loginBit = 1;
        } else {
            onLoginFailed();
            return;
        }
        //3초동안 progressbar 돌리기,그리고 현재화면 종료 TODO: 3초뒤 프로세스가 마무리 되는것이기 때문에 이전에 WAS와 통신을 하고 결과 bit만 아래에 넘겨줄 것.
        final int finalLoginBit = loginBit;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (finalLoginBit == 1) {
                            onLoginSuccess();
                        } else
                            onLoginFailed();


                        // On complete call either onLoginSuccess or onLoginFailed
                        //onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 1000);
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

    private String getUUID() {
        String encodedWidevineId = "";
        encodedWidevineId = preferences.getString("UUID", "null");

        if (!encodedWidevineId.equals("null"))
            return encodedWidevineId;


        //기기 키값 받아오는 내용
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        UUID WIDEVINE_UUID = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
        MediaDrm wvDrm;
        try {

            wvDrm = new MediaDrm(WIDEVINE_UUID);

            //이제부터 사용할 키값
            byte[] widevineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
            String aa = widevineId.toString();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                encodedWidevineId = Base64.getEncoder().encodeToString(widevineId).trim();
            }
        } catch (UnsupportedSchemeException e) {
            e.printStackTrace();
        }

        editor.putString("UUID", encodedWidevineId);
        editor.commit();
        return encodedWidevineId;
    }
}