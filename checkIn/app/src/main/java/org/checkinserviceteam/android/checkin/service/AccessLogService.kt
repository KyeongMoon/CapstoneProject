package org.checkinserviceteam.android.checkin.service

import org.checkinserviceteam.android.checkin.data.LoginJSONData
import retrofit2.Call
import retrofit2.http.*

interface AccessLogService {

    @Headers("content-type: application/json")
    @POST("checkIN/test")
    fun signUp(
        @Body body : LoginJSONData
    ): Call<LoginJSONData>

}