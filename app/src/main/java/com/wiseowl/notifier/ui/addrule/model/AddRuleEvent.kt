package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.RepeatType
import com.wiseowl.notifier.domain.event.Event

sealed class AddRuleEvent: Event() {
    data object CreateRule : AddRuleEvent()
    data class OnChangeRuleName(val name: String): AddRuleEvent()
    data class OnChangeRuleDescription(val description: String): AddRuleEvent()
    data object OnClickSelectLocationField: AddRuleEvent()
    data object CloseLocationDialog: AddRuleEvent()
    data class OnChangeRuleLocation(val location: Location): AddRuleEvent()
    data class OnQueryPlace(val search: String): AddRuleEvent()
    data class OnPlaceSuggestionUpdated(val suggestions: List<Suggestion>?): AddRuleEvent()
    data class OnSuggestionSelected(val suggestion: Suggestion): AddRuleEvent()
    data object OnClickActionType: AddRuleEvent()
    data object OnClickRepeatType: AddRuleEvent()
    data class OnChangeRuleActionType(val actionType: ActionType): AddRuleEvent()
    data class OnChangeRuleRepeatType(val repeatType: RepeatType): AddRuleEvent()
    data class OnChangeRuleRadius(val radiusInMeters: String): AddRuleEvent()
    data class OnChangeRuleDelay(val delayInMinutes: String): AddRuleEvent()
}