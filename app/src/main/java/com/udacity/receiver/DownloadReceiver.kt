package com.udacity.receiver

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.R
import com.udacity.utils.sendNotification

open class DownloadReceiver: BroadcastReceiver(){

    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager: DownloadManager

    private var _downloadID = MutableLiveData<Long>()
    val downloadID: LiveData<Long>
        get() = _downloadID

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "it is download receiver", Toast.LENGTH_SHORT).show()
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        val query = DownloadManager.Query()
        query.setFilterById(id)

        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    notificationManager.sendNotification(
                        context?.getString(R.string.download_complete).toString(),
                        context
                    )
                    Toast.makeText(
                        context,
                        "DLoading complete, downloadID: ${downloadID.value} ",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("dl_success", "download was successful")
                }
                DownloadManager.STATUS_FAILED -> {
//                        Toast.makeText(context, "DLoading failed, ", Toast.LENGTH_SHORT).show()
                    Log.e("dl_fail", "onReceive() failed")
                    throw ExceptionInInitializerError("a download failure occured")
                }
            }
        }


        val notificationManager = ContextCompat.getSystemService(
            context!!,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            context.getText(R.string.download_complete).toString(),
            context
        )

    }

}
