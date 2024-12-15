package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Place
import com.wiseowl.notifier.ui.common.model.InputField

data class AddRuleState(
    val ruleName: InputField<String> = InputField(label = "name"),
    val ruleDescription: InputField<String> = InputField(label = "description"),
    val place: InputField<Place> = InputField(label = "Place"),
    val ruleRadius: InputField<Int> = InputField(value = 0, label = "effecting area radius"),
    val actionType: InputField<ActionType> = InputField(label = "action type"),
    val ruleDelay: InputField<Int> = InputField(label = "notifier delay")
)