package com.wiseowl.notifier.data.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class Notification {
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, id: String, name: String, importance: Int) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(
            NotificationChannel(
                id, name, importance
            )
        )
    }
}