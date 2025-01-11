package com.wiseowl.notifier.data.service.worker

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.wiseowl.notifier.data.di.ServiceLocator
import com.wiseowl.notifier.data.service.notification.Notification
import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.RepeatType
import com.wiseowl.notifier.domain.service.LocationService.Companion.getDistanceFromLatLonInMeters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

class NotifierWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    init {
        ServiceLocator.initialize(applicationContext)
    }
    private val locationService = ServiceLocator.getLocationService()
    override suspend fun doWork(): Result {
        if (!ServiceLocator.getAuthenticator().isLoggedIn()) return Result.failure()

        val location = locationService.getCurrentLocation(applicationContext, Dispatchers.IO)
            ?: return Result.failure()
        val longitude = location.longitude
        val latitude = location.latitude

        val rules = ServiceLocator.getRulesRepository().getRules().firstOrNull()
            ?: return Result.failure()

        rules.forEach { rule ->
            val ruleLongitude = rule.location.longitude
            val ruleLatitude = rule.location.latitude
            val distanceInMeters = getDistanceFromLatLonInMeters(
                Location(
                    longitude = longitude,
                    latitude = latitude
                ), Location(longitude = ruleLongitude, latitude = ruleLatitude)
            )

            val isInRange = (distanceInMeters - rule.radiusInMeter) < 0

            when (rule.actionType) {
                ActionType.ENTERING -> {
                    if (isInRange && rule.active) {
                        Notification().notify(
                            rule.id,
                            title = "This is a reminder notification".toUpperCase(Locale.current),
                            subtitle = "This is the reminder for ${rule.name} since your have entered the location"
                        )
                        val updatedRule = rule.copy(active = false)
                        ServiceLocator.getRulesRepository().updateRule(updatedRule)

                    } else if (!isInRange && rule.repeatType == RepeatType.REPEAT && rule.active) {
                        val updatedRule = rule.copy(active = true)
                        ServiceLocator.getRulesRepository().updateRule(updatedRule)
                    }
                }

                ActionType.LEAVING -> {
                    if (!isInRange && rule.active) {
                        Notification().notify(
                            rule.id,
                            title = "This is a reminder notification".toUpperCase(Locale.current),
                            subtitle = "This is the reminder for ${rule.name} since your have entered the location"
                        )
                        val updatedRule = rule.copy(active = false)
                        ServiceLocator.getRulesRepository().updateRule(updatedRule)
                    } else if (isInRange && rule.repeatType == RepeatType.REPEAT && !rule.active) {
                        val updatedRule = rule.copy(active = true)
                        ServiceLocator.getRulesRepository().updateRule(updatedRule)
                    }
                }
            }
        }
        return Result.success()
    }

    companion object {
        const val NAME = "NotifierWorker"

        fun schedule(applicationContext: Context) {
            val workRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
                NotifierWorker::class.java,
                PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                TimeUnit.MILLISECONDS
            ).addTag(NAME).build()
            WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest,
            )
        }
    }
}