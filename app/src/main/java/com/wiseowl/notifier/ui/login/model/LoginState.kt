package com.wiseowl.notifier.ui.login.model

import com.wiseowl.notifier.ui.common.model.InputField

data class LoginState(
    val isUserLoggedIn: Boolean = false,
    val email: InputField<String> = InputField("", label = "Email"),
    val password: InputField<String> = InputField("", label = "Password")
)