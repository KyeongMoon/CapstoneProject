package org.checkinserviceteam.android.checkin.data.DTO

data class M_RemoteLogoutDTO(
    var agentID: String,
    var deviceID: String,
    var result: Boolean,
    var jwt: String
)