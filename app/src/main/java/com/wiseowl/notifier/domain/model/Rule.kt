package com.wiseowl.notifier.domain.model

data class Rule(
    val id: Int = 0,
    val name: String,
    val description: String?,
    val place: Place?,
    val radiusInMeter: Int = 10,
    val active: Boolean?,
    val actionType: ActionType?,
    val delayInMinutes: Int = 0
)