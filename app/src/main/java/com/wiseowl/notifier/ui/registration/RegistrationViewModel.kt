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
                _state.update { newState -> newState.copy(firstName = newState.firstName.copy(value = event.value)) }
            }
            is RegistrationEvent.EditLastName -> viewModelScope.launch {
                _state.update { newState -> newState.copy(lastName = newState.lastName.copy(value = event.value)) }
            }
            is RegistrationEvent.EditEmail -> viewModelScope.launch {
                _state.update { newState -> newState.copy(email = newState.email.copy(value = event.value)) }
            }
            is RegistrationEvent.EditPassword -> viewModelScope.launch {
                _state.update{ newState -> newState.copy(password = newState.password.copy(value = event.value)) }
            }
            is RegistrationEvent.Register -> {
                ProgressBarEvent.send(true)
                ServiceLocator.getAuthenticator().signUp(event.email, event.password){ result: Result ->
                    when(result){
                        is Result.Failure -> SnackBarEvent.send(result.error?.message.toString())
                        is Result.Success<*> -> {
                            viewModelScope.launch(Dispatchers.IO) {
                                ProgressBarEvent.send(true)
                                saveUserInfo(result.data as AuthResult)
                                _state.update{ newState -> newState.copy(isUserLoggedIn = true) }
                                ProgressBarEvent.send(false)
                            }
                        }
                    }
                    ProgressBarEvent.send(false)
                }
            }

        }
    }

    private suspend fun saveUserInfo(authResult: AuthResult) {
        val user = User(
            userId = authResult.user?.uid.toString(),
            email = authResult.user?.email.toString(),
            firstName = state.value.firstName.value,
            lastName = state.value.lastName.value,
            profilePicture = null
        )

        UserRepositoryImpl.getInstance().saveUser(user)
    }

}