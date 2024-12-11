package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.Rule

sealed interface AddRuleEvent{
    data class CreateRule(val rule: Rule) : AddRuleEvent
    data class OnChangeRuleName(val name: String): AddRuleEvent
    data class OnChangeRuleDescription(val description: String): AddRuleEvent
    data class OnChangeRuleLocation(val location: Location): AddRuleEvent
    data class OnChangeRuleActionType(val actionType: ActionType): AddRuleEvent
    data class OnChangeRuleRadius(val radiusInMeters: Int): AddRuleEvent
    data class OnChangeRuleDelay(val delayInMinutes: Int): AddRuleEvent
}