package org.checkinserviceteam.android.checkin.service

import org.checkinserviceteam.android.checkin.data.DTO.M_LoginDTO
import retrofit2.Call
import retrofit2.http.*

interface LoginService {

    @Headers("content-type: application/json")
    @POST("checkIN/signIn_M")
    fun signIn(
        @Body body : M_LoginDTO
    ): Call<M_LoginDTO>

    @Headers("content-type: application/json")
    @POST("checkIN/signOut_M")
    fun signOut(
        @Body body : M_LoginDTO
    ): Call<M_LoginDTO>
}