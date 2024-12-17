package com.wiseowl.notifier.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.wiseowl.notifier.data.local.repository.UserRepositoryImpl
import com.wiseowl.notifier.data.ServiceLocator
import com.wiseowl.notifier.domain.event.ProgressBarEvent
import com.wiseowl.notifier.domain.util.Result
import com.wiseowl.notifier.domain.event.SnackBarEvent
import com.wiseowl.notifier.domain.model.User
import com.wiseowl.notifier.ui.registration.model.RegistrationEvent
import com.wiseowl.notifier.ui.registration.model.RegistrationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel: ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> get() = _state

    fun onEvent(event: RegistrationEvent){
        when(event){
            is RegistrationEvent.EditFirstName -> viewModelScope.launch {
                _state.update { newState -> newState.copy(firstName = newState.firstName.updateValue(value = event.value)) }
            }
            is RegistrationEvent.EditLastName -> viewModelScope.launch {
                _state.update { newState -> newState.copy(lastName = newState.lastName.updateValue(value = event.value)) }
            }
            is RegistrationEvent.EditEmail -> viewModelScope.launch {
                _state.update { newState -> newState.copy(email = newState.email.updateValue(value = event.value)) }
            }
            is RegistrationEvent.EditPassword -> viewModelScope.launch {
                _state.update{ newState -> newState.copy(password = newState.password.updateValue(value = event.value)) }
            }
            is RegistrationEvent.Register -> {
                ProgressBarEvent.send(true)
                val email = state.value.email.value
                val password = state.value.password.value
                if(email.isNullOrEmpty()) {
                    _state.update{ newState -> newState.copy(email = newState.email.copy(error = "email is required")) }
                    SnackBarEvent.send("Email cannot be empty")
                    return
                } else if(password.isNullOrEmpty()) {
                    _state.update{ newState -> newState.copy(password = newState.password.copy(error = "password is required")) }
                    SnackBarEvent.send("Password cannot be empty")
                    return
                }
                ServiceLocator.getAuthenticator().signUp(email, password){ result: Result ->
                    when(result){
                        is Result.Failure -> SnackBarEvent.send(result.error?.message.toString())
                        is Result.Success<*> -> {
                            viewModelScope.launch(Dispatchers.IO) {
                                ProgressBarEvent.send(true)
                                val authResult = result.data as AuthResult
                                saveUserInfo(
                                    User(
                                        userId = authResult.user?.uid.toString(),
                                        email = authResult.user?.email.toString(),
                                        firstName = state.value.firstName.value.toString(),
                                        lastName = state.value.lastName.value.toString(),
                                        profilePicture = null
                                    )
                                )
                                ProgressBarEvent.send(false)
                            }
                        }
                    }
                    ProgressBarEvent.send(false)
                }
            }

        }
    }

    private suspend fun saveUserInfo(user: User) {
        UserRepositoryImpl.getInstance().saveUser(user)
    }

}