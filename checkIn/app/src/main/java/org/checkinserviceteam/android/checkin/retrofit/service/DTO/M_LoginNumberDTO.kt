package org.checkinserviceteam.android.checkin.retrofit.service.DTO

data class M_LoginNumberDTO(
    var agentID: String?,
    var deviceID: String?,
    var LoginNumber: String?,
    var result: Boolean?,
    var jwt: String?
){
    constructor(agentID: String, deviceID: String, jwt: String) :this(
        agentID,
        deviceID,
        "-1",
        true,
        jwt
    )
}