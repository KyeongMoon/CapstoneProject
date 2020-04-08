package org.capstone.android.checkin.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.capstone.android.checkin.R;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class CreateOTPActivity extends AppCompatActivity {

    TextView otpTextView;
    TextView otpProgressbarSec;
    ProgressBar otpProgressBar;
    CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_otp);

        otpTextView = findViewById(R.id.createOTPTextView);
        otpProgressbarSec = findViewById(R.id.OTPProgressbarSec);
        otpProgressBar = findViewById(R.id.OTPProgressbar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String encodedWidevineId = preferences.getString("UUID",null);

        mCountDownTimer = new CountDownTimer(1000 * 60 * 60, 1000) {
            int i = 61;

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);

                if (i == 0 || i == 61) {
                    int OTP = UpdateOTP(encodedWidevineId);
                    otpTextView.setText(Integer.toString(OTP));
                    i = 60;
                }
                otpProgressBar.setProgress(i);
                otpProgressbarSec.setText(i + " 초");
                i--;
            }

            @Override
            public void onFinish() {
                //nothing
            }
        };
        mCountDownTimer.start();

    }

    private static int UpdateOTP(String UUID) {
        long time = new Date().getTime();
        try {
            return verify_code(UUID, time);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int verify_code(String key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t / 1000;

        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        SecretKey signKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[20 - 1] & 0xF;

        long truncatedHash = 0;
        for (int i = 0; i < 4; i++) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 900000;
        truncatedHash += 100000;

        return (int) truncatedHash;
    }

    public static boolean checkCode(String userCode, String otpkey) {
        long otpnum = Integer.parseInt(userCode);
        long wave = new Date().getTime();
        boolean result = false;
        try {
            int window = 1000 * 60;
            for (int i = -window; i <= 2000; i += 1000) {
                long hash = verify_code(otpkey, wave + i);
                if (hash == otpnum)
                    result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void FinishActivity(View view) {
        mCountDownTimer.onFinish();
        finish();
    }
}
