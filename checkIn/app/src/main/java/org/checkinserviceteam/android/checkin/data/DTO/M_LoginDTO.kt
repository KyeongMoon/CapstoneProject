package org.checkinserviceteam.android.checkin.data.DTO

data class M_LoginDTO(
    var agentID: String?,
    var agentPW: String?,
    var deviceID: String?,
    var deviceName: String?,
    var result: Int?,
    var jwt: String?
) {
    constructor(agentID: String,jwt: String) : this(
        agentID,
        "",
        "",
        "",
        0,
        jwt
    )
    constructor(agentID: String, agentPW: String, deviceID: String, deviceName: String) : this(
        agentID,
        agentPW,
        deviceID,
        deviceName,
        0,
        ""
    )
}