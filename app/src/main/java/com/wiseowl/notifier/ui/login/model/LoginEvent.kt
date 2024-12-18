package com.wiseowl.notifier.ui.login.model

import com.wiseowl.notifier.ui.Event

sealed class LoginEvent: Event() {
    data class EditUserName(val value: String): LoginEvent()
    data class EditPassword(val value: String): LoginEvent()
    data class Login(val email: String, val password: String): LoginEvent()
}