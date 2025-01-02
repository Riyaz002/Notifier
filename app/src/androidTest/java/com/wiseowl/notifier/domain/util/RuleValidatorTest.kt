package com.wiseowl.notifier.domain.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.RepeatType
import com.wiseowl.notifier.domain.model.Rule
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RuleValidatorTest{

    @Test
    fun isRuleValidReturnsFalseIfNameIsEmpty(){
        val rule = Rule(
            id = 0,
            name = "",
            description = "description",
            location = Location(0.0,0.0),
            radiusInMeter = 11.0,
            active = true,
            actionType = ActionType.ENTERING,
            repeatType = RepeatType.ONCE,
            delayInMinutes = 1
        )

        val result = RuleValidator(rule).isRuleValid()

        Assert.assertFalse(result)
    }

    @Test
    fun isRuleValidReturnsFalseIfRadiusIsInvalid(){
        val rule = Rule(
            id = 0,
            name = "Name",
            description = "description",
            location = Location(0.0,0.0),
            radiusInMeter = -11.0,
            active = true,
            actionType = ActionType.ENTERING,
            repeatType = RepeatType.ONCE,
            delayInMinutes = 1
        )

        val result = RuleValidator(rule).isRuleValid()

        Assert.assertFalse(result)
    }

    @Test
    fun isRuleValidReturnsFalseIfDelayIsInvalid(){
        val rule = Rule(
            id = 0,
            name = "Name",
            description = "description",
            location = Location(0.0,0.0),
            radiusInMeter = 11.0,
            active = true,
            actionType = ActionType.ENTERING,
            repeatType = RepeatType.ONCE,
            delayInMinutes = -1
        )

        val result = RuleValidator(rule).isRuleValid()

        Assert.assertFalse(result)
    }

    @Test
    fun isRuleValidReturnsTrueAllValuesAreValid(){
        val rule = Rule(
            id = 0,
            name = "Name",
            description = "description",
            location = Location(0.0,0.0),
            radiusInMeter = 11.0,
            active = true,
            actionType = ActionType.ENTERING,
            repeatType = RepeatType.ONCE,
            delayInMinutes = 1
        )

        val result = RuleValidator(rule).isRuleValid()

        Assert.assertTrue(result)
    }
}