package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.ui.common.model.InputField

data class AddRuleState(
    val ruleName: InputField<String> = InputField("", label = "name"),
    val ruleDescription: InputField<String> = InputField("", "description"),
    val location: InputField<Location>? = InputField(Location(0.0,0.0) , "name"),
    val ruleRadius: InputField<Int> = InputField(10, "radius"),
    val ruleType: InputField<ActionType> = InputField(ActionType.ENTERING, "action type"),
    val ruleDelay: InputField<Int> = InputField(0 ,"delay in minute")
)