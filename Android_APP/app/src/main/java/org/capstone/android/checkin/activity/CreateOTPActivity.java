package org.capstone.android.checkin.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.smuemsw.capstone.R;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class CreateOTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_otp);


        HashMap map = this.generate("user", "host");

        TextView textView = findViewById(R.id.createOTPTextView);
        textView.setText(map.get("encodedKey").toString());


//        for (int i = 0; i < 100000; i++) {
//            String a = Integer.toString(i);
            if (checkCode("123456" , map.get("encodedKey").toString())) {
                textView.setText("success");
//            }
        }
    }

    public HashMap<String, String> generate(String userName, String hostName) {
        HashMap<String, String> map = new HashMap<String, String>();
        byte[] buffer = new byte[5 + 5 * 5];
        new Random().nextBytes(buffer);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] secretKey = Arrays.copyOf(buffer, 10);
            byte[] bEncodedKey = encoder.encode(secretKey);

            String encodedKey = new String(bEncodedKey);
            String url = "url";

            map.put("encodedKey", encodedKey);

        }

        return map;
    }

    public boolean checkCode(String userCode, String otpkey) {
        long otpnum = Integer.parseInt(userCode);
        long wave = new Date().getTime() / 30000;
        boolean result = false;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] decodedKey = decoder.decode(otpkey);
                int window = 3;
                for (int i = -window; i <= window; i++) {
                    long hash = verify_code(decodedKey, wave + i);
                    if (hash == otpnum) result = true;
                }
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
