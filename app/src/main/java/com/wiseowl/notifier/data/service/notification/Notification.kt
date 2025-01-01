package com.wiseowl.notifier.data.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.riyaz.dakiya.Dakiya
import com.riyaz.dakiya.core.model.Message
import com.riyaz.dakiya.core.notification.Style

class Notification {
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "default", "notifier", NotificationManager.IMPORTANCE_HIGH
            )
        )
    }

    /**
     * Send general/default notification.
     */
    fun notify(id: Int, title: String, subtitle: String){
        val message = Message(
            id,
            title = title,
            subtitle = subtitle,
            style = Style.DEFAULT,
            channelID = "default"
        )

        Dakiya.showNotification(message)
    }
}