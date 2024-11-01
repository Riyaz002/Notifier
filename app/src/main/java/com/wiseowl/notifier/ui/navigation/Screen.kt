package com.wiseowl.notifier.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen

@Serializable
data object Home: Screen
@Serializable
data object Profile: Screen
@Serializable
data object AddRule: Screen
@Serializable
data object Setting: Screen