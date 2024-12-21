package com.wiseowl.notifier.data.service.worker

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.riyaz.dakiya.Dakiya
import com.riyaz.dakiya.core.model.Message
import com.riyaz.dakiya.core.notification.Style
import com.wiseowl.notifier.data.local.database.NotifierDatabase
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
            val message = Message(
                rule.id,
                title = "This is a reminder notification".toUpperCase(Locale.current),
                style = Style.DEFAULT
            )

            if(isInRange && rule.actionType == ActionType.ENTERING){
                Dakiya.showNotification(message.copy(subtitle = "This is the reminder for ${rule.title} since your have entered ${rule.place.name}"))
            } else if(!isInRange && rule.actionType == ActionType.LEAVING){
                Dakiya.showNotification(message.copy(subtitle = "This is the reminder for ${rule.title} since your have left ${rule.place.name}"))
            }
        }
        return Result.success()
    }

    companion object{
        //don't change UUID_STRING, Changing this will result in creation of new worker without cancelling the old one.
        const val UUID_STRING = "0f14d0ab-9605-4a62-a9e4-5ed26688389b"
    }
}