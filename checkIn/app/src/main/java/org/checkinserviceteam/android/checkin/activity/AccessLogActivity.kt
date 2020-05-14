package org.checkinserviceteam.android.checkin.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_access_log.*
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.adater.AccessLogListViewAdapter
import org.checkinserviceteam.android.checkin.data.AccessLogListViewData
import org.checkinserviceteam.android.checkin.retrofit.service.LoginNumberService
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


        //TODO : 네트워크 관련 외부에 선언
        var retrofit = MyApplication.getRetrofit()
        val createLoginNumberService = retrofit.create(LoginNumberService::class.java)
        // var data

        activity_access_log_bt_access_log.setOnClickListener {
            //createLoginNumberService.

            //onResponse에서 수신한 데이터 지역변수에 bind 해주기
            //이후 adapter 달아주어 리셋
            //네트워킹 동안 progress bar


        }

    }
    fun FinishActivity(view: View){
        finish()
    }
}
