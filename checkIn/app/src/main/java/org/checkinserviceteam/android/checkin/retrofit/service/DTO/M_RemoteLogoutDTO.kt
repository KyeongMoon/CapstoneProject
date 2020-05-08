package org.checkinserviceteam.android.checkin.retrofit.service.DTO

data class M_RemoteLogoutDTO(
    var agentID: String?,
    var deviceID: String?,
    var result: Boolean?,
    var jwt: String?
){
    constructor(agentID: String, deviceID: String, jwt: String):this(
        agentID,
        deviceID,
        true,
        jwt
    )
}