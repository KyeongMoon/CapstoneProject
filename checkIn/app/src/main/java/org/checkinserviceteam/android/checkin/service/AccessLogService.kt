package org.checkinserviceteam.android.checkin.service

import org.checkinserviceteam.android.checkin.data.DTO.M_AccessLogDTO
import retrofit2.Call
import retrofit2.http.*
//TODO : implementation

interface AccessLogService {

    @Headers("content-type: application/json")
    @POST("checkIN/accessLog")
    fun signUp(
        @Body body : M_AccessLogDTO
    ): Call<M_AccessLogDTO>

}