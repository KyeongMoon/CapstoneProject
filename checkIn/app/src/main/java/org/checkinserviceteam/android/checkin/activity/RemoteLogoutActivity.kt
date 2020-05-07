package org.checkinserviceteam.android.checkin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_remote_logout.*
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.service.RemoteLogoutService

class RemoteLogoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_logout)

        activity_remote_logout_bt_logout
        activity_remote_logout_tv_logout

        var retrofit = MyApplication.getRetrofit()
        val remoteLogoutService = retrofit.create(RemoteLogoutService::class.java)

        //var sendData

        activity_remote_logout_bt_logout.setOnClickListener {
            remoteLogoutService
        }
    }
}
