package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Place
import com.wiseowl.notifier.ui.Event

sealed class AddRuleEvent: Event() {
    data object CreateRule : AddRuleEvent()
    data class OnChangeRuleName(val name: String): AddRuleEvent()
    data class OnChangeRuleDescription(val description: String): AddRuleEvent()
    data class OnChangeRulePlace(val place: Place): AddRuleEvent()
    data class OnChangeRuleActionType(val actionType: ActionType): AddRuleEvent()
    data class OnChangeRuleRadius(val radiusInMeters: Int): AddRuleEvent()
    data class OnChangeRuleDelay(val delayInMinutes: Int): AddRuleEvent()
}