package com.wiseowl.notifier.ui.registration.model

sealed class RegistrationEvent {
    data class EditFirstName(val value: String): RegistrationEvent()
    data class EditLastName(val value: String): RegistrationEvent()
    data class EditEmail(val value: String): RegistrationEvent()
    data class EditPassword(val value: String): RegistrationEvent()
    data object Register: RegistrationEvent()
}