package org.checkinserviceteam.android.checkin.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import kotlinx.android.synthetic.main.activity_create_login_number.*
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.data.DTO.M_LoginNumberDTO
import org.checkinserviceteam.android.checkin.service.LoginNumberService

class CreateLoginNumberActivity : AppCompatActivity() {

    lateinit var mCountDownTimer: CountDownTimer
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_login_number)

        preferences = MyApplication.getPreference()

        //TODO : 네트워크 선언은 thread 외부에 선언, data var 재사용
        var retrofit = MyApplication.getRetrofit()
        val createLoginNumberService = retrofit.create(LoginNumberService::class.java)
        val sendData =
            M_LoginNumberDTO(
                preferences.getString("idPref", "").toString(),
                preferences.getString("deviceIdPref", "").toString()
            )

        mCountDownTimer = object : CountDownTimer(1000 * 60 * 60, 1000){
            private var i = 61

            override fun onTick(millisUntilFinished: Long) {
                Log.v("Log_tag", "Tick of Progress$i$millisUntilFinished")

                if(i == 0 || i == 61){

                    //TODO : 서버와 1분단위 연결 및 서버요청 대기 시 원형 progressbar


                    //TODO : 서버에 요청과 수신하여 tv에 반영



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
