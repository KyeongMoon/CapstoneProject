package org.capstone.android.checkin.http;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.capstone.android.checkin.MyApplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestHttpConnection {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public String request(String _url, Object _params){

        preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        editor = preferences.edit();

        HttpURLConnection urlConn = null;
        String page = "";

        /**
         * 1. HttpURLConnection을 통해 web의 데이터를 가져온다.
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

            String tt = preferences.getString("Authorization", "fuck");

            //TODO : 송신할 경우 수정
            if(!tt.equals("fuck")) {
                Log.d("wwwwwwwww",tt);
                urlConn.addRequestProperty("Authorization", tt);
                urlConn.setRequestProperty("Authorization", tt);
                urlConn.addRequestProperty("jwt", tt);
                urlConn.setRequestProperty("jwt", tt);
                editor.putString("Authorization", "fuck");
                editor.commit();
            }

            //전달받은 객체를 json 형식 String으로 전환
            ObjectMapper mapper = new ObjectMapper();
            String sendData = mapper.writeValueAsString(_params);

            Log.d("RequestHttpConnection", sendData);

            // [2-2]. parameter 전달 및 데이터 읽어오기.
            //String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
            OutputStream os = urlConn.getOutputStream();
            os.write(sendData.getBytes("UTF-8")); // 출력 스트림에 출력.
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.


            // [2-3]. 연결 요청 확인.
            // 실패 시 (실패 string)null을 리턴하고 메서드를 종료.
//            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
//                return "HTTP_OK 실패";

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
                return response;
            }


            // [2-4]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            // 출력물의 라인과 그 합에 대한 변수.
            String line;

            if(tt.equals("fuck")){
//                String a = urlConn.getHeaderField("Authorization");
//                Log.d("jwtttttt", a);
//                editor.putString("Authorization", a);
                editor.commit();
            }


            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null){
                page += line + "\n";
            }
            Log.d("RequestHttpConnection", page);

            return page;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }


        return null;

    }
}
