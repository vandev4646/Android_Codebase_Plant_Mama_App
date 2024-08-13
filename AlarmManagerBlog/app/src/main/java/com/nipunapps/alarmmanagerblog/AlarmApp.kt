package com.nipunapps.alarmmanagerblog

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class AlarmApp : Application() {

    override fun onCreate() {
        super.onCreate()
        //creating a notification channel
        val channelId = "alarm_id"
        val channelName = "alarm_name"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
}