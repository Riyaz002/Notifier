package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.ui.Event

sealed class AddRuleEvent: Event() {
    data object CreateRule : AddRuleEvent()
    data class OnChangeRuleName(val name: String): AddRuleEvent()
    data class OnChangeRuleDescription(val description: String): AddRuleEvent()
    data object OnClickSelectLocationField: AddRuleEvent()
    data object CloseLocationDialog: AddRuleEvent()
    data class OnChangeRuleLocation(val location: Location): AddRuleEvent()
    data class OnCurrentLocationUpdate(val location: Location): AddRuleEvent()
    data class OnQueryPlace(val search: String): AddRuleEvent()
    data class OnPlaceSuggestionUpdated(val suggestions: List<Suggestion>?): AddRuleEvent()
    data class OnSuggestionSelected(val suggestion: Suggestion): AddRuleEvent()
    data class OnChangeRuleActionType(val actionType: ActionType): AddRuleEvent()
    data class OnChangeRuleRadius(val radiusInMeters: String): AddRuleEvent()
    data class OnChangeRuleDelay(val delayInMinutes: Int): AddRuleEvent()
}