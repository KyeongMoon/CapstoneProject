package org.checkinserviceteam.android.checkin.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_remote_logout.*
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.retrofit.service.DTO.M_RemoteLogoutDTO
import org.checkinserviceteam.android.checkin.retrofit.service.RemoteLogoutService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteLogoutActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_logout)

        preferences = MyApplication.getPreference()

        val currId = preferences.getString("idPref", "").toString()
        val currDeviceId = preferences.getString("deviceIdPref", "").toString()
        val currJwt = preferences.getString("jwtPref", "").toString()

        activity_remote_logout_bt_logout
        activity_remote_logout_tv_logout

        var retrofit = MyApplication.getRetrofit()
        val remoteLogoutService = retrofit.create(RemoteLogoutService::class.java)
        val sendData = M_RemoteLogoutDTO(currId, currDeviceId, currJwt)

        activity_remote_logout_bt_logout.setOnClickListener {
            remoteLogoutService.requestRemoteLogout(sendData).enqueue(object :
                Callback<M_RemoteLogoutDTO> {
                override fun onFailure(call: Call<M_RemoteLogoutDTO>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(
                    call: Call<M_RemoteLogoutDTO>,
                    response: Response<M_RemoteLogoutDTO>
                ) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
    fun FinishActivity(view: View){
        finish()
    }
}
