package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.Place
import com.wiseowl.notifier.ui.common.model.InputField

data class AddRuleState(
    val ruleName: InputField<String> = InputField(value = "", label = "name"),
    val ruleDescription: InputField<String> = InputField(value = "",label = "description"),
    val place: InputField<Place> = InputField(value = Place("Place name", Location(0.0,0.0)), label = "Place"),
    val ruleRadius: InputField<Int> = InputField(value = 0, label = "effecting area radius"),
    val actionType: InputField<ActionType> = InputField(value = ActionType.ENTERING, label = "action type"),
    val ruleDelay: InputField<Int> = InputField(value = 0,label = "notifier delay")
)