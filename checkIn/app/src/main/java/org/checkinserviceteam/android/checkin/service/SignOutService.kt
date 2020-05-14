package org.checkinserviceteam.android.checkin.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.retrofit.service.DTO.M_LoginDTO
import org.checkinserviceteam.android.checkin.retrofit.service.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignOutService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("onTaskRemoved", "start")

//        var preferences =
//            MyApplication.getPreference()
//
//        val currId = preferences.getString("idPref", "error").toString()
//        val currJwt = preferences.getString("jwtPref", "").toString()
//
//        var sendData = M_LoginDTO(currId, currJwt)
//        var retrofit =
//            MyApplication.getRetrofit()
//        var logoutService = retrofit.create(LoginService::class.java)
//
//
//        //do nothing
//        logoutService.signOut(sendData)?.enqueue(object : Callback<M_LoginDTO> {
//            override fun onFailure(call: Call<M_LoginDTO>, t: Throwable) {
//                Log.d("SignOutService", "fail")
//
//            }
//
//            override fun onResponse(call: Call<M_LoginDTO>, response: Response<M_LoginDTO>) {
//                Log.d("SignOutService", "success")
//            }
//        })

        Thread.sleep(1000)
        Log.d("onTaskRemoved", "end")

        this.stopSelf()
    }
}