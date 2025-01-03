package com.wiseowl.notifier.data.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.wiseowl.notifier.R

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
    fun Context.notify(id: Int, title: String, subtitle: String){
        val notification = NotificationCompat.Builder(this, "default")
        notification
            .setContentTitle(title)
            .setContentText(subtitle)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
        val notificationManager = this.getSystemService(NotificationManager::class.java)
        notificationManager.notify(id, notification.build())
        //TODO: This version of Dakiya have issues sending notification
//        val message = Message(
//            id,
//            title = title,
//            subtitle = subtitle,
//            style = Style.DEFAULT,
//            channelID = "default"
//        )
//
//        Dakiya.showNotification(message)
    }
}