package org.checkinserviceteam.android.checkin.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.retrofit.service.DTO.M_LoginDTO
import org.checkinserviceteam.android.checkin.retrofit.service.LoginService
import org.checkinserviceteam.android.checkin.service.SignOutService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this, SignOutService::class.java))

        preferences = MyApplication.getPreference()

        Thread.setDefaultUncaughtExceptionHandler(MyApplication.getAndroidDefault())
    }

    override fun onDestroy() {
        super.onDestroy()
        //stopService(Intent(this, SignOutService::class.java))
        Log.d("onDestroy", "start")


        val currId = preferences.getString("idPref", "error").toString()
        val currJwt = preferences.getString("jwtPref", "").toString()

        var sendData = M_LoginDTO(currId, currJwt)
        var retrofit =
            MyApplication.getRetrofit()
        var logoutService = retrofit.create(LoginService::class.java)


        //do nothing
        logoutService.signOut(sendData)?.enqueue(object : Callback<M_LoginDTO> {
            override fun onFailure(call: Call<M_LoginDTO>, t: Throwable) {
                Log.d("MainActivity", "fail")

            }

            override fun onResponse(call: Call<M_LoginDTO>, response: Response<M_LoginDTO>) {
                Log.d("MainActivity", "success")
            }
        })

        Log.d("onDestroy", "end")



    }

    fun moveCreateLoginNumberActivity(view: View) {
        startActivity(Intent(this, CreateLoginNumberActivity::class.java))
    }

    fun moveAccessLogActivity(view: View) {
        startActivity(Intent(this, AccessLogActivity::class.java))
    }

    fun moveCreateOTPActivity(view: View) {
        startActivity(Intent(this, CreateOTPActivity::class.java))
    }

    fun moveRemoteLogoutActivity(view: View) {
        startActivity(Intent(this, RemoteLogoutActivity::class.java))
    }

    fun moveFingerLoginActivity(view: View) {
        startActivity(Intent(this, FingerLoginActivity::class.java))
    }
}