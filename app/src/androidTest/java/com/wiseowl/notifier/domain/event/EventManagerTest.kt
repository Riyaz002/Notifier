package com.wiseowl.notifier.domain.event

import androidx.test.filters.SmallTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@SmallTest
class EventManagerTest{

    @Test
    fun sendAndSubscribeAreWorking() = runTest{
        var event: Event?= null
        EventManager.subscribe {
            event = it
        }

        EventManager.send(ProgressBar(true))
        advanceUntilIdle()

        Thread.sleep(1000)

        Assert.assertEquals(ProgressBar(true), event)
    }
}