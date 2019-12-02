package com.ccooy.apidemos.app

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.ccooy.apidemos.R

class LocalService : Service() {
    private var mNM: NotificationManager? = null

    private val NOTIFICATION = R.string.local_service_started

    private val mBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        internal val service: LocalService
            get() = this@LocalService
    }

    override fun onCreate() {
        mNM = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        showNotification()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("LocalService", "Received start id $startId: $intent")
        return Service.START_STICKY
    }

    override fun onDestroy() {
        mNM!!.cancel(NOTIFICATION)
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    private fun showNotification() {
        val text = getText(R.string.local_service_started)

        val notification = Notification(
            R.drawable.stat_sample, text,
            System.currentTimeMillis()
        )

        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, LocalServiceActivities.Controller::class.java), 0
        )

        notification.setLatestEventInfo(
            this, getText(R.string.local_service_label),
            text, contentIntent
        )

        mNM!!.notify(NOTIFICATION, notification)
    }
}