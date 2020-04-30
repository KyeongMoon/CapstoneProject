package org.checkinserviceteam.android.checkin.data

data class LoginJSONData(
    var agentID: String,
    var agentPW: String,
    var uuid: String,
    var jwtString : String?,
    var result: Boolean
)
{
    constructor(agentID: String, agentPW: String, UUID: String) : this(
        agentID,
        agentPW,
        UUID,
        "",
        false
    )
}