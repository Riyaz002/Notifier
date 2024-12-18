package com.wiseowl.notifier.ui

import com.wiseowl.notifier.ui.navigation.Screen

open class Event

data class SnackBar(val text: String): Event()
data class Navigate(val screen: Screen): Event()
data object PopBackStack: Event()
data class ProgressBar(val show: Boolean): Event()