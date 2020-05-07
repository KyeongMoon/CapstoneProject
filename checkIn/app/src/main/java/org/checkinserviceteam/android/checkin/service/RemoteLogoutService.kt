package org.checkinserviceteam.android.checkin.service

import org.checkinserviceteam.android.checkin.data.DTO.M_RemoteLogoutDTO
import retrofit2.Call
import retrofit2.http.*
//TODO : implementation
interface RemoteLogoutService {

    @Headers("content-type: application/json")
    @POST("checkIN/remoteSignOut")
    fun signUp(
        @Body body : M_RemoteLogoutDTO
    ): Call<M_RemoteLogoutDTO>

}