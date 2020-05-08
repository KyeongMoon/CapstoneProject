package org.checkinserviceteam.android.checkin.retrofit.service

import org.checkinserviceteam.android.checkin.retrofit.service.DTO.M_RemoteLogoutDTO
import retrofit2.Call
import retrofit2.http.*
//TODO : implementation
interface RemoteLogoutService {

    @Headers("content-type: application/json")
    @POST("checkIN/remoteSignOut")
    fun requestRemoteLogout(
        @Body body : M_RemoteLogoutDTO
    ): Call<M_RemoteLogoutDTO>

}