package com.wiseowl.notifier.ui.home.model

import com.wiseowl.notifier.domain.event.Event

sealed class HomeEvent: Event(){
    data class DeleteRule(val ruleId: Int) : HomeEvent()
}