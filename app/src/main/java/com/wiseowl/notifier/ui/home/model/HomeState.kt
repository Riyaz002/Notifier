package com.wiseowl.notifier.ui.home.model

import com.wiseowl.notifier.domain.model.Rule
import com.wiseowl.notifier.domain.model.User

data class HomeState(
    val user: User? = null,
    val rules: List<Rule> = listOf()
)