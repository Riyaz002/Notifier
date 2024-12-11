package com.wiseowl.notifier.ui.navigation

interface Action

data class Navigate(val screen: Screen): Action
data class Pop(val inclusive: Screen? = null): Action