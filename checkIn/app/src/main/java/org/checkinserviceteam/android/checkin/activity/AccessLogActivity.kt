package org.checkinserviceteam.android.checkin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_access_log.*
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.adater.AccessLogListViewAdapter
import org.checkinserviceteam.android.checkin.data.AccessLogListViewData
import java.util.*

class AccessLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access_log)

        var accessLogDataList = ArrayList<AccessLogListViewData>()

        accessLogDataList.add(AccessLogListViewData("시간 1", "IP 1", "위치 1"))
        accessLogDataList.add(AccessLogListViewData("시간 2", "IP 2", "위치 2"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))
        accessLogDataList.add(AccessLogListViewData("시간 3", "IP 3", "위치 3"))

        activity_access_log_lv_access_log.adapter = (AccessLogListViewAdapter(this, accessLogDataList))


    }
}
