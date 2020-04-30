package org.checkinserviceteam.android.checkin.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val preferences = MyApplication.getPreference()
//        val editor = preferences.edit()
//
//        editor.putString("testT", "hi").apply()

        startActivity(Intent(this, LoginActivity::class.java))

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