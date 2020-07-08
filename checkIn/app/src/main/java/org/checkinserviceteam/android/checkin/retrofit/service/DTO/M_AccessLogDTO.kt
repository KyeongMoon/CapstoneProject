package org.checkinserviceteam.android.checkin.retrofit.service.DTO

import org.checkinserviceteam.android.checkin.retrofit.service.DTO.item.M_AccessLogItem

data class M_AccessLogDTO(
    var agentID: String,
    var jwt: String,
    var result: Boolean,
    var accessLogItemArrayList: List<M_AccessLogItem>
) {
    constructor(agentID: String, jwt: String) : this(
        agentID,
        jwt,
        true,
        listOf()
    )
}