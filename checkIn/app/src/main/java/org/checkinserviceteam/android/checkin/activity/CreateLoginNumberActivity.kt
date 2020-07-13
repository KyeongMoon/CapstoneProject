package org.checkinserviceteam.android.checkin.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_login_number.*
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.adater.LoadingDialog
import org.checkinserviceteam.android.checkin.retrofit.service.DTO.M_LoginNumberDTO
import org.checkinserviceteam.android.checkin.retrofit.service.LoginNumberService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CreateLoginNumberActivity : AppCompatActivity() {

    lateinit var mCountDownTimer: CountDownTimer
    lateinit var preferences: SharedPreferences
    lateinit var retrofit: Retrofit
    lateinit var createLoginNumberService: LoginNumberService
    lateinit var sendData: M_LoginNumberDTO
    lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_login_number)

        preferences = MyApplication.getPreference()

        retrofit = MyApplication.getRetrofit()
        createLoginNumberService = retrofit.create(LoginNumberService::class.java)
        sendData =
            M_LoginNumberDTO(
                preferences.getString("idPref", "").toString(),
                preferences.getString("deviceIdPref", "").toString(),
                preferences.getString("jwtPref","").toString()
            )
        loadingDialog = LoadingDialog(this)
    }

    override fun onStart() {
        super.onStart()

        mCountDownTimer = object : CountDownTimer(1000 * 60 * 60, 1000){
            private var i = 21

            override fun onTick(millisUntilFinished: Long) {
                Log.v("Log_tag", "Tick of Progress$i$millisUntilFinished")

                if(i == 0 || i == 21){
                    loadingDialog.startLoadingDialog()

                    createLoginNumberService.requestLoginNumber(sendData).enqueue(object: Callback<M_LoginNumberDTO>{
                        override fun onFailure(call: Call<M_LoginNumberDTO>, t: Throwable) {
                            Log.d("onFailure", t.toString())
                            loadingDialog.dismissDialog()
                            Toast.makeText(applicationContext, "인터넷 연결을 확인해주세요.", Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<M_LoginNumberDTO>, response: Response<M_LoginNumberDTO>) {
                            var result = response.body()!!.result
                            if (result == 1) {
                                val loginNumber: String? = response.body()!!.loginNumber
                                activity_create_login_number_tv_login_number.text =
                                    loginNumber!!.substring(0, 4) + " " + loginNumber!!.substring(
                                        4,
                                        8
                                    )
                            }
                            else
                                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                            loadingDialog.dismissDialog()
                        }
                    })

                    i = 20
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

    override fun onStop() {
        super.onStop()
        mCountDownTimer.cancel()
    }
    fun FinishActivity(view: View){
        finish()
    }
}
