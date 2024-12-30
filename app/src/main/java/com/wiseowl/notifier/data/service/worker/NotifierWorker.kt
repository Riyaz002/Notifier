package com.wiseowl.notifier.data.service.worker

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.wiseowl.notifier.data.ServiceLocator
import com.wiseowl.notifier.data.local.database.NotifierDatabase
import com.wiseowl.notifier.data.local.database.entity.RuleEntity.Companion.toRule
import com.wiseowl.notifier.data.service.location.LocationService
import com.wiseowl.notifier.data.service.notification.Notification
import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.RepeatType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class NotifierWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    private val locationService = LocationService.getInstance(context)
    override suspend fun doWork(): Result {
        val location = locationService.getCurrentLocation(applicationContext, Dispatchers.IO)
            ?: return Result.failure()
        val longitude = location.longitude
        val latitude = location.latitude

        val rules = NotifierDatabase.getInstance(applicationContext).dao.getRules().firstOrNull()
            ?: return Result.failure()

        rules.forEach { rule ->
            val ruleLongitude = rule.location.longitude
            val ruleLatitude = rule.location.latitude
            val distanceInMeters = locationService.getDistanceFromLatLonInMeters(
                Location(
                    longitude = longitude,
                    latitude = latitude
                ), Location(longitude = ruleLongitude, latitude = ruleLatitude)
            )

            val isInRange = (distanceInMeters - rule.radiusInMeter) < 0

            if (rule.actionType == ActionType.ENTERING) {
                if (isInRange && rule.active) {
                    Notification().notify(
                        rule.id,
                        title = "This is a reminder notification".toUpperCase(Locale.current),
                        subtitle = "This is the reminder for ${rule.title} since your have entered the location"
                    )
                    val updatedRule = rule.copy(active = false)
                    ServiceLocator.getRulesRepository().updateRule(updatedRule.toRule())
                } else if (!isInRange && rule.repeatType == RepeatType.REPEAT) {
                    val updatedRule = rule.copy(active = true)
                    ServiceLocator.getRulesRepository().updateRule(updatedRule.toRule())
                }
            }

            if (rule.actionType == ActionType.ENTERING) {
                if (!isInRange && rule.active) {
                    Notification().notify(
                        rule.id,
                        title = "This is a reminder notification".toUpperCase(Locale.current),
                        subtitle = "This is the reminder for ${rule.title} since your have entered the location"
                    )
                    val updatedRule = rule.copy(active = false)
                    ServiceLocator.getRulesRepository().updateRule(updatedRule.toRule())
                } else if (isInRange && rule.repeatType == RepeatType.REPEAT) {
                    val updatedRule = rule.copy(active = true)
                    ServiceLocator.getRulesRepository().updateRule(updatedRule.toRule())
                }
            }
        }
        return Result.success()
    }

    companion object {
        const val NAME = "NotifierWorker"
    }
}