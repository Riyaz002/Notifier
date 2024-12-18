package com.wiseowl.notifier.ui.registration.model

import com.wiseowl.notifier.ui.Event

sealed class RegistrationEvent: Event() {
    data class EditFirstName(val value: String): RegistrationEvent()
    data class EditLastName(val value: String): RegistrationEvent()
    data class EditEmail(val value: String): RegistrationEvent()
    data class EditPassword(val value: String): RegistrationEvent()
    data object Register: RegistrationEvent()
}