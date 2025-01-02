package com.wiseowl.notifier.domain.model

data class Rule(
    val id: Int = 0,
    val name: String,
    val description: String?,
    val location: Location,
    val radiusInMeter: Double = 10.0,
    val active: Boolean,
    val actionType: ActionType?,
    val repeatType: RepeatType?,
    val delayInMinutes: Int = 0
)