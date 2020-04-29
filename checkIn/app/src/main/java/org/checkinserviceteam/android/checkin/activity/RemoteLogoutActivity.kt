package org.checkinserviceteam.android.checkin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_remote_logout.*
import org.checkinserviceteam.android.checkin.R

class RemoteLogoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_logout)

        activity_remote_logout_bt_logout
        activity_remote_logout_tv_logout


    }
}
