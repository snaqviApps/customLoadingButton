package com.udacity.main.ui

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.udacity.receiver.DownloadReceiver

class LoadingButtonViewModel (application: Application): AndroidViewModel(application) {

    private val REQUEST_CODE = 0
    private val TRIGGER_TIME = "TRIGGER_AT"

    private val minute: Long = 60_000L
    private val second: Long = 1_000L

//    private val notifyPendingIntent: PendingIntent

    private val notifyIntent = Intent(application, DownloadReceiver::class.java)

    val _downloadIDObserver = MutableLiveData<Int>()
    val dowloadIdObserver : MutableLiveData<Int>
        get() {
            return _downloadIDObserver
        }

//    init {
//        notifyPendingIntent = PendingIntent.getBroadcast(
//            getApplication(),
//            REQUEST_CODE,
//            notifyIntent,
//            PendingIntent.FLAG_NO_CREATE
//        )
//    }

}