package org.checkinserviceteam.android.checkin.data

import com.google.gson.annotations.SerializedName

data class LoginJSONData(
    @SerializedName("agentID")
    var agentID: String,
    @SerializedName("agentPW")
    var agentPW: String,
    @SerializedName("UUID")
    var UUID: String,
    @SerializedName("jwtString")
    var jwtString: String,
    @SerializedName("result")
    var result: Boolean
) {
    constructor(agentID: String, agentPW: String, UUID: String) : this(
        agentID,
        agentPW,
        UUID,
        "",
        false
    )
}