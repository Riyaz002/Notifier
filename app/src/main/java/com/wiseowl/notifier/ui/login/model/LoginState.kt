package com.wiseowl.notifier.ui.login.model

data class LoginState(
    val isUserLoggedIn: Boolean = false,
    val email: String = "",
    val password: String = ""
)