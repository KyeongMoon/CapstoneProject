package org.checkinserviceteam.android.checkin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import kotlinx.android.synthetic.main.activity_create_login_number.*
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.service.LoginService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class CreateLoginNumberActivity : AppCompatActivity() {

    lateinit var mCountDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_login_number)

        /*var retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(JacksonConverterFactory.create())
            .client(createOkHttpClient())
            .build()

        var loginService : LoginService = retrofit.create(LoginService::class.java)*/

        var retrofit = MyApplication.getRetrofit()



        mCountDownTimer = object : CountDownTimer(1000 * 60 * 60, 1000){
            private var i = 61

            override fun onTick(millisUntilFinished: Long) {
                Log.v("Log_tag", "Tick of Progress$i$millisUntilFinished")

                if(i == 0 || i == 61){

                    //TODO : 서버와 1분단위 연결 및 서버요청 대기 시 원형 progressbar


                    //TODO : 서버에 요청과 수신하여 tv에 반영
                    //activity_create_login_number_tv_login_number.text =

                    i = 60
                }
                activity_create_login_number_pb_login_number.progress = i
                activity_create_login_number_tv_sec.text = "$i 초"
                i--
            }

            override fun onFinish() {
                mCountDownTimer.cancel()
            }
        }

        mCountDownTimer.start()

    }

    override fun onDestroy() {
        mCountDownTimer.onFinish()
        super.onDestroy()
    }
}
