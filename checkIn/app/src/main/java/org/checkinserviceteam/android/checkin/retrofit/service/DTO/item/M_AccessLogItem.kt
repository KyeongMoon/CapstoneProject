package org.checkinserviceteam.android.checkin.retrofit.service.DTO.item

data class M_AccessLogItem(
    var agentID: String,
    var accessIP: String,
    var accessTime: String,
    var loginStatus: String
)