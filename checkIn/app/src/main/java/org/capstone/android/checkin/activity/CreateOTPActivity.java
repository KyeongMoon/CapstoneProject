package org.capstone.android.checkin.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.capstone.android.checkin.R;
import org.capstone.android.checkin.data.OtpJSONData;
import org.capstone.android.checkin.http.RequestHttpConnection;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class CreateOTPActivity extends AppCompatActivity {

    TextView otpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_otp);

        otpTextView = findViewById(R.id.createOTPTextView);

        //키 생성인데 사실상 필요없고 기기값 가져와서 전송해주자.
        HashMap map = this.generate("user", "host");
        otpTextView.setText(map.get("encodedKey").toString());




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
                String encodedWidevineId = Base64.getEncoder().encodeToString(widevineId).trim();

                otpTextView.setText("start " +widevineId.length+"\n"+ widevineId +"\n" + aa +"\n");
            }
        } catch (UnsupportedSchemeException e) {
            e.printStackTrace();
        }




        try {
            //URL 설정.
            String url = "http://18.218.11.150:8080/checkIN/otp";

            OtpJSONData otpData = new OtpJSONData();
            long time = new Date().getTime();
            int ret = verify_code("[B@9909bab",time);

            otpData.setOtpCode(Integer.toString(ret));
            otpData.setOtpKey("[B@9909bab");

            // AsyncTask를 통해 HttpURLConnection 수행.
            NetworkTask networkTask = new NetworkTask(url, otpData);
            networkTask.execute();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
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

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private Object values;

        public NetworkTask(String url, Object values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpConnection requestHttpURLConnection = new RequestHttpConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            otpTextView.setText(s);
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
