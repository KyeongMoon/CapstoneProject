package org.checkinserviceteam.android.checkin.data.DTO

data class M_AccessLogDTO(
    var agentID: String,
    var deviceID: String,
    var accessLogItemArrayList: ArrayList<M_AccessLogItem>,
    var jwt: String,
    var result: Boolean
)