package com.udacity.receiver

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.udacity.R
import com.udacity.utils.sendNotification

open class DownloadReceiver : BroadcastReceiver() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager: DownloadManager

    override fun onReceive(context: Context?, intent: Intent?) {

        downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        val downloadID = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)

        val query = DownloadManager.Query()
        query.setFilterById(id!!)

        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    notificationManager.sendNotification(
                        context.getString(R.string.download_complete),
                        context
                    )
                    Toast.makeText(
                        context,
                        "DLoading complete, downloadID: $downloadID ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                DownloadManager.STATUS_FAILED -> {
                    throw ExceptionInInitializerError("a download failure occurred")
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

