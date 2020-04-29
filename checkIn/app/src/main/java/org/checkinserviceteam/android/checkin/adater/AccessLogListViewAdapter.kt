package org.checkinserviceteam.android.checkin.adater

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_access_log_listview.view.*
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.data.AccessLogListViewData
import java.util.ArrayList

class AccessLogListViewAdapter(
    var mContext: Context,
    var itemList: ArrayList<AccessLogListViewData>
) : BaseAdapter() {

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_access_log_listview, null)

        view.item_access_log_listview_tv_time.text = itemList[position].time
        view.item_access_log_listview_tv_ip.text = itemList[position].ip
        view.item_access_log_listview_tv_status.text = itemList[position].status

        return view
    }
}