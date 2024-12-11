package com.wiseowl.notifier.ui.registration.model

import com.wiseowl.notifier.ui.common.model.InputField

data class RegistrationState(
    val isUserLoggedIn: Boolean = false,
    val firstName: InputField<String> = InputField("", label = "First Name"),
    val lastName: InputField<String> = InputField("", label = "Last Name"),
    val email: InputField<String> = InputField("", label = "Email"),
    val password: InputField<String> = InputField("", label = "Password")
)