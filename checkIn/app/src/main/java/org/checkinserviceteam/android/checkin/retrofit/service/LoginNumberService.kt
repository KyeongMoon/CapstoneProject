package org.checkinserviceteam.android.checkin.retrofit.service

import org.checkinserviceteam.android.checkin.retrofit.service.DTO.M_LoginNumberDTO
import retrofit2.Call
import retrofit2.http.*

interface LoginNumberService {
//TODO : implementation

    @Headers("content-type: application/json")
    @POST("checkIN/loginNumber")
    fun requestLoginNumber(
        @Body body : M_LoginNumberDTO
    ): Call<M_LoginNumberDTO>

}