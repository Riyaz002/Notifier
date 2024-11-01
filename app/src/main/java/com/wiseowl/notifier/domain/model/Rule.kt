package com.wiseowl.notifier.domain.model

data class Rule(
    val id: Int,
    val title: String,
    val description: String?,
    val location: Location,
    val radiusInMeter: Int,
    val active: Boolean,
    val actionType: ActionType,
    val delayInMinutes: Int
)