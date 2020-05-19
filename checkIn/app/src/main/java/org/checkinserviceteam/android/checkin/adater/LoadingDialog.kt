package org.checkinserviceteam.android.checkin.adater

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import org.checkinserviceteam.android.checkin.R

class LoadingDialog {
    lateinit var activity: Activity
    lateinit var dialog: AlertDialog

    constructor(activity: Activity) {
        this.activity = activity
    }

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}