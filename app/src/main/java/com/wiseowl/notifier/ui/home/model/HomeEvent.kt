package com.wiseowl.notifier.ui.home.model

sealed interface HomeEvent{
    data object CreateRule: HomeEvent
    data class DeleteRule(val ruleId: Int) : HomeEvent
}