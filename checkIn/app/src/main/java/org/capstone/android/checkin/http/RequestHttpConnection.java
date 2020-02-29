package org.capstone.android.checkin.http;

import android.content.ContentValues;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.capstone.android.checkin.data.JSONData;
import org.capstone.android.checkin.data.OtpJSONData;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RequestHttpConnection {
    public String request(String _url, Object _params){
        HttpURLConnection urlConn = null;
        String page = "";

        /**
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         * */
        try{
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();

            // [2-1]. urlConn 설정.
            urlConn.setRequestMethod("GET"); // URL 요청에 대한 메소드 설정 : POST.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            //urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");
            //urlConn.setRequestProperty("Content_Type", "application/json;charset=UTF-8");
            urlConn.setRequestProperty("Context_Type", "application/json");
            urlConn.setDoOutput(true);


            //전달받은 객체를 json 형식 String으로 전환
            ObjectMapper mapper = new ObjectMapper();
            String sendData = mapper.writeValueAsString(_params);

            //전송 데이터 출력
            page += "SEND\n" + sendData + "\n";



            // [2-2]. parameter 전달 및 데이터 읽어오기.
            //String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
            page += "2-2";
            OutputStream os = urlConn.getOutputStream();
            page += "2-2";
            os.write(sendData.getBytes("UTF-8")); // 출력 스트림에 출력.
            page += "2-2";
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.


            // [2-3]. 연결 요청 확인.
            // 실패 시 (실패 string)null을 리턴하고 메서드를 종료.
//            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
//                return "HTTP_OK 실패";

            page += "2-3";
            if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {

            } else {
                //에러난 문구 출력
                InputStream is = urlConn.getErrorStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();
                String response = new String(byteData);
                page += "에러" + response;
            }


            // [2-4]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            page += "2-4";
            // 출력물의 라인과 그 합에 대한 변수.
            String line;

            // 라인을 받아와 합친다.

            while ((line = reader.readLine()) != null){
                page += line + "\n";
            }

            return page;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }

        return page + "연결실패";

    }
}
