package com.wiseowl.notifier.ui.login.model

sealed class LoginEvent {
    data class EditUserName(val value: String): LoginEvent()
    data class EditPassword(val value: String): LoginEvent()
    data class Login(val email: String, val password: String): LoginEvent()
}