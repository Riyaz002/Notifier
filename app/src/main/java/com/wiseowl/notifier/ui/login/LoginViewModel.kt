package com.wiseowl.notifier.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiseowl.notifier.domain.ServiceLocator
import com.wiseowl.notifier.domain.event.ProgressBarEvent
import com.wiseowl.notifier.domain.util.Result
import com.wiseowl.notifier.domain.event.SnackBarEvent
import com.wiseowl.notifier.ui.login.model.LoginEvent
import com.wiseowl.notifier.ui.login.model.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> get() = _state

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.EditUserName -> {
                viewModelScope.launch {
                    _state.update { loginState -> loginState.copy(email = event.value) }
                }
            }

            is LoginEvent.EditPassword -> {
                viewModelScope.launch {
                    _state.update{ loginState -> loginState.copy(password = event.value) }
                }
            }

            is LoginEvent.Login -> {
                ProgressBarEvent.send(true)
                ServiceLocator.getAuthenticator().signIn(event.email, event.password){ result: Result ->
                    when(result){
                        is Result.Failure -> SnackBarEvent.send(result.error?.message.toString())
                        is Result.Success<*> -> _state.update{ loginState -> loginState.copy(isUserLoggedIn = true) }
                    }
                    ProgressBarEvent.send(false)
                }
            }

            is LoginEvent.Register -> {
                ProgressBarEvent.send(true)
                ServiceLocator.getAuthenticator().signUp(event.email, event.password){ result: Result ->
                    when(result){
                        is Result.Failure -> SnackBarEvent.send(result.error?.message.toString())
                        is Result.Success<*> -> _state.update{ loginState -> loginState.copy(isUserLoggedIn = true) }
                    }
                    ProgressBarEvent.send(false)
                }
            }
        }
    }

}