package com.wiseowl.notifier.login.model

sealed class LoginEvent {
    data class EditUserName(val value: String): LoginEvent()
    data class EditPassword(val value: String): LoginEvent()
    data object Login: LoginEvent()
}