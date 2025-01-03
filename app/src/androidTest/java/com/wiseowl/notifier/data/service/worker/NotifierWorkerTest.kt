package com.wiseowl.notifier.data.service.worker

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.wiseowl.notifier.data.di.ServiceLocator
import com.wiseowl.notifier.data.local.database.NotifierDatabase
import com.wiseowl.notifier.data.local.database.entity.RuleEntity.Companion.toRuleEntity
import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.RepeatType
import com.wiseowl.notifier.domain.model.Rule as NotifierRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class NotifierWorkerTest {

    private val requiredRules = arrayListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).also {
        if(Build.VERSION_CODES.S<=Build.VERSION.SDK_INT) it.add(Manifest.permission.POST_NOTIFICATIONS)
    }.toTypedArray()

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(*requiredRules)

    private val database = NotifierDatabase.getInstance(getApplicationContext())

    @Before
    fun setUp() {
        runBlocking {
            ServiceLocator.initialize(getApplicationContext())
            database.dao.insertRule(
                NotifierRule(
                    name = "Some name",
                    description = "Some description",
                    location = Location(1.0, 1.0),
                    actionType = ActionType.ENTERING,
                    repeatType = RepeatType.REPEAT,
                    radiusInMeter = 11.0,
                    delayInMinutes = 1,
                    active = true
                ).toRuleEntity()
            )
        }
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(getApplicationContext(), config)
    }

    @Test
    fun notifierWorkerFiringNotification() {
        val request = PeriodicWorkRequestBuilder<NotifierWorker>(
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MILLISECONDS
        ).build()

        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
            NotifierWorker.NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )

        val testDriver = WorkManagerTestInitHelper.getTestDriver(getApplicationContext())

        testDriver!!.setPeriodDelayMet(request.id)
    }
}