package com.wiseowl.notifier.data.service.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.wiseowl.notifier.data.local.NotifierDatabase
import com.wiseowl.notifier.data.service.location.LocationService
import com.wiseowl.notifier.data.util.LocationDistanceCalculator
import com.wiseowl.notifier.domain.model.ActionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class NotifierWorker(context: Context, parameters: WorkerParameters): CoroutineWorker(context, parameters) {
    private val locationService = LocationService(context)
    override suspend fun doWork(): Result {
        val location = locationService.getCurrentLocation(applicationContext, Dispatchers.IO) ?: return Result.failure()
        val longitude = location.longitude
        val latitude = location.latitude


        val rules = NotifierDatabase.getInstance(applicationContext).dao.getRules().firstOrNull() ?: return Result.failure()

        rules.forEach { rule ->
            val ruleLongitude = rule.place.location.longitude
            val ruleLatitude = rule.place.location.latitude
            val distanceInMeters = LocationDistanceCalculator(longitude, latitude, ruleLongitude, ruleLatitude).getDistanceFromLatLonInMeters()
            val isInRange = (distanceInMeters - rule.radiusInMeter) < 0
            val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)

            val notification = NotificationCompat.Builder(applicationContext, "sasc").build()
            if(isInRange && rule.actionType == ActionType.ENTERING){
                notificationManager.notify(rule.id, notification)
            } else if(!isInRange && rule.actionType == ActionType.LEAVING){
                notificationManager.notify(rule.id, notification)
            }
        }
        return Result.success()
    }
}