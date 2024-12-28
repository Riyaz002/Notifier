package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.RepeatType
import com.wiseowl.notifier.ui.common.model.InputField

data class AddRuleState(
    val ruleName: InputField<String> = InputField(value = "", label = "name"),
    val ruleDescription: InputField<String> = InputField(value = "",label = "description"),
    val selectedPlaceName: InputField<String> = InputField(value = "", label = "Place", enabled = false),
    val locationSelectorExpandedState: Boolean = false,
    val actionTypeSelectorExpandedState: Boolean = false,
    val repeatTypeSelectorExpandedState: Boolean = false,
    val selectedPlaceLocation: Location? = null,
    val suggestions: List<Suggestion> = listOf(),
    val actionType: InputField<ActionType> = InputField(value = ActionType.ENTERING, label = "action type", enabled = false),
    val repeatType: InputField<RepeatType> = InputField(value = RepeatType.ONCE, label = "repeat type", enabled = false),
    val ruleRadius: InputField<Double> = InputField(value = 150.0, label = "effecting area radius", enabled = false),
    val ruleDelay: InputField<Int> = InputField(value = 5,label = "notifier delay")
)