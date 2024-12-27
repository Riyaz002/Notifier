package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.ui.common.model.InputField

data class AddRuleState(
    val ruleName: InputField<String> = InputField(value = "", label = "name"),
    val ruleDescription: InputField<String> = InputField(value = "",label = "description"),
    val selectedPlaceName: InputField<String> = InputField(value = "", label = "Place"),
    val selectedPlaceLocation: Location? = null,
    val currentLocation: Location = Location(77.0, 28.0),
    val suggestions: List<Suggestion> = listOf(),
    val actionType: InputField<ActionType> = InputField(value = ActionType.ENTERING, label = "action type"),
    val ruleRadius: InputField<Int> = InputField(value = 500, label = "effecting area radius"),
    val ruleDelay: InputField<Int> = InputField(value = 0,label = "notifier delay")
)