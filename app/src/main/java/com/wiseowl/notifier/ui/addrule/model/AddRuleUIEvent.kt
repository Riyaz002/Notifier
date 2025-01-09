package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.domain.event.Event

sealed class AddRuleUIEvent: Event() {
    data class SearchPlace(val searchKey: String) : AddRuleUIEvent()
}