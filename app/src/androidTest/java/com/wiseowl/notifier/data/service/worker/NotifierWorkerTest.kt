package com.wiseowl.notifier.data.service.worker

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.wiseowl.notifier.data.ServiceLocator
import com.wiseowl.notifier.data.local.database.NotifierDatabase
import com.wiseowl.notifier.data.local.database.entity.RuleEntity.Companion.toRuleEntity
import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.RepeatType
import com.wiseowl.notifier.domain.model.Rule
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Before
import org.junit.Test

class NotifierWorkerTest{

    val database = androidx.room.Room.inMemoryDatabaseBuilder(getApplicationContext(), NotifierDatabase::class.java).build()

    @Before
    fun setUp(){
        runBlocking {
            database.dao.insertRule(
                Rule(
                    name = "Some name",
                    description = "Some description",
                    location = ServiceLocator.getLocationService().getCurrentLocation(
                        getApplicationContext(), StandardTestDispatcher(TestCoroutineScheduler(), "Name")
                    )!!,
                    actionType = ActionType.ENTERING,
                    repeatType = RepeatType.REPEAT,
                    radiusInMeter = 111111.0,
                    delayInMinutes = 1,
                    active = true
                ).toRuleEntity()
            )
        }
    }

    @Test
    fun notifierWorkerFiringNotification(){

    }
}