package org.capstone.android.checkin.activity;

import android.content.Context;
import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.capstone.android.checkin.R;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

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
        otpProgressBar = (ProgressBar) findViewById(R.id.OTPProgressbar);

        String encodedWidevineId = "";


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


        //TODO : final로 선언해줘야 타이머 안에 돌아가는데 원래 이값은 로그인할때 정리가 되어있어야한다. 혹은 sharedpreference에 저장된 값 불러오기
        final String finalEncodedWidevineId = encodedWidevineId;

        mCountDownTimer = new CountDownTimer(1000 * 60 * 60, 1000) {
            int i = 61;

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);

                if (i == 0 || i == 61) {
                    //TODO: otp 갱신 작성
                    int OTP = UpdateOTP(finalEncodedWidevineId);
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
        long value = t / 30000;

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
        truncatedHash %= 1000000;

        return (int) truncatedHash;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkCode(String userCode, String otpkey) {
        long otpnum = Integer.parseInt(userCode);
        long wave = new Date().getTime() / 30000;
        boolean result = false;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decodedKey = decoder.decode(otpkey);
            int window = 3;
            for (int i = -window; i <= window; i++) {
                long hash = verify_code(decodedKey, wave + i);
                if (hash == otpnum) result = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        SecretKey signKey = new SecretKeySpec(key, "HmacSHA1");
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
        truncatedHash %= 1000000;

        return (int) truncatedHash;
    }
}
