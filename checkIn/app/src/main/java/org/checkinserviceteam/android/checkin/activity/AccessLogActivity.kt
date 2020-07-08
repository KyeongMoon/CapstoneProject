package org.checkinserviceteam.android.checkin.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_access_log.*
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.adater.AccessLogListViewAdapter
import org.checkinserviceteam.android.checkin.adater.LoadingDialog
import org.checkinserviceteam.android.checkin.data.AccessLogListViewData
import org.checkinserviceteam.android.checkin.retrofit.service.AccessLogService
import org.checkinserviceteam.android.checkin.retrofit.service.DTO.M_AccessLogDTO
import org.checkinserviceteam.android.checkin.retrofit.service.DTO.item.M_AccessLogItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AccessLogActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access_log)

        preferences = MyApplication.getPreference()

        var accessLogDataList = ArrayList<AccessLogListViewData>()

        val loadingDialog = LoadingDialog(this)
        val currId = preferences.getString("idPref", "").toString()
        val currJwt = preferences.getString("jwtPref", "").toString()
        val mContext = applicationContext

        var retrofit = MyApplication.getRetrofit()
        val accessLogService = retrofit.create(AccessLogService::class.java)
        var sendData = M_AccessLogDTO(currId, currJwt)

        activity_access_log_bt_access_log.setOnClickListener {

            loadingDialog.startLoadingDialog()

            accessLogService.requestLog(sendData).enqueue(object : Callback<M_AccessLogDTO> {
                override fun onFailure(call: Call<M_AccessLogDTO>, t: Throwable) {
                    Log.d("onFailure", t.toString())
                    Toast.makeText(applicationContext, "인터넷 연결을 확인해주세요.", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<M_AccessLogDTO>,
                    response: Response<M_AccessLogDTO>
                ) {
                    val accessLogList = response.body()!!.accessLogItemArrayList

                    accessLogDataList.clear()

                    //정렬
                    Collections.reverse(accessLogList)

                    for(i in 0 until accessLogList.size){
                        accessLogDataList.add(AccessLogListViewData(accessLogList[i].accessTime.substring(0, 16).replace(' ', '\n'), accessLogList[i].accessIP, accessLogList[i].loginStatus.replace("(", "\n(")))
                    }

                    activity_access_log_lv_access_log.adapter =
                        (AccessLogListViewAdapter(mContext, accessLogDataList))

                    loadingDialog.dismissDialog()
                }
            })


        }
    }

    fun FinishActivity(view: View) {
        finish()
    }
}
