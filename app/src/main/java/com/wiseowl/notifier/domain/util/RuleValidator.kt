package com.wiseowl.notifier.domain.util

import com.wiseowl.notifier.domain.model.Rule

class RuleValidator(val rule: Rule) {
    fun isRuleValid(): Boolean{
        return rule.name.isNotEmpty() && rule.radiusInMeter>0 && rule.delayInMinutes>0
    }
}