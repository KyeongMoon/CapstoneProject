package org.checkinserviceteam.android.checkin.retrofit.service.DTO

import org.checkinserviceteam.android.checkin.retrofit.service.DTO.item.M_AccessLogItem

data class M_AccessLogDTO(
    var agentID: String,
    var deviceID: String,
    var accessLogItemArrayList: ArrayList<M_AccessLogItem>,
    var jwt: String,
    var result: Boolean
)