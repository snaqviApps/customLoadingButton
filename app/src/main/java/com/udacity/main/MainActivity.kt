package com.udacity.main

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.R
import com.udacity.main.ui.LoadingButtonViewModel
import com.udacity.receiver.DownloadReceiver
import com.udacity.ui.LoadingButtonViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var downloadManager: DownloadManager
    private val receiver = DownloadReceiver()

    private var _downloadID = MutableLiveData<Long>()
    val downloadID: LiveData<Long>
        get() = _downloadID

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val LoadViewModelFactory = LoadingButtonViewModelFactory(application)
//      mainActivityViewModel = ViewModelProvider(this, LoadViewModelFactory).get(LoadingButtonViewModel::class.java) // error: PendingIntent is getting null
        val mainActivityViewModel: LoadingButtonViewModel by viewModels()

        /**
         * This approach for register works, as AndroidMenifest.xml file-based registeration did not
         * */
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button.setOnClickListener {
            download()
        }

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /** Create Channel to send notification for file download-complete, status*/
        createChannel(
            this.getString(R.string.download_notification_channel_id),
            this.getString(R.string.download_notification_channel_name)
        )
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        _downloadID.value = downloadManager.enqueue(request)
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.LTGRAY
            notificationChannel.enableVibration(true)
            notificationChannel.description = this.getString(R.string.download_complete)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}

