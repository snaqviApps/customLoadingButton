package com.udacity.main

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.udacity.R
import com.udacity.main.ui.LoadingButtonViewModel
import com.udacity.ui.LoadingButtonViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

//    private var downloadID: Long = 0
    private var _downloadID = MutableLiveData<Long>()
    val downloadID: LiveData<Long>
    get() = _downloadID

    private lateinit var mainActivityViewModel: LoadingButtonViewModel

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val LoadViewModelFactory = LoadingButtonViewModelFactory(application)
        mainActivityViewModel = ViewModelProvider(this, LoadViewModelFactory).get(LoadingButtonViewModel::class.java)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        _downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.



        /** Create Channel to send notification for file download-complete, status*/
        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name))
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    private fun createChannel(channelId: String, channelName: String){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.LTGRAY
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download complete"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
