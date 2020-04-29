package org.checkinserviceteam.android.checkin.service

import org.checkinserviceteam.android.checkin.data.LoginJSONData
import retrofit2.Call
import retrofit2.http.*

interface LoginService {

    @POST("/checkIN/test2")
    fun signup(
        @Body body: LoginJSONData
    ): Call<LoginJSONData>
}