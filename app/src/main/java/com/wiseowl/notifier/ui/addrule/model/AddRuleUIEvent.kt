package com.wiseowl.notifier.ui.addrule.model

import com.wiseowl.notifier.ui.Event

sealed class AddRuleUIEvent: Event() {
    data class SearchPlace(val searchKey: String) : AddRuleUIEvent()
}