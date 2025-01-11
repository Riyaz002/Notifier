package com.wiseowl.notifier.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.wiseowl.notifier.data.di.ServiceLocator
import com.wiseowl.notifier.domain.event.EventManager
import com.wiseowl.notifier.domain.util.Result
import com.wiseowl.notifier.domain.event.Event
import com.wiseowl.notifier.domain.event.Navigate
import com.wiseowl.notifier.domain.event.PopBackStack
import com.wiseowl.notifier.domain.event.ProgressBar
import com.wiseowl.notifier.domain.event.SnackBar
import com.wiseowl.notifier.ui.login.model.LoginEvent
import com.wiseowl.notifier.ui.login.model.LoginState
import com.wiseowl.notifier.ui.navigation.Home
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> get() = _state

    fun onEvent(event: Event){
        when(event){
            is LoginEvent.EditUserName -> {
                viewModelScope.launch {
                    _state.update { loginState -> loginState.copy(email = loginState.email.copy(value = event.value)) }
                }
            }

            is LoginEvent.EditPassword -> {
                viewModelScope.launch {
                    _state.update{ loginState -> loginState.copy(password = loginState.password.updateValue(value = event.value)) }
                }
            }

            is LoginEvent.Login -> {
                EventManager.send(ProgressBar(true))
                ServiceLocator.getAuthenticator().signIn(event.email, event.password){ result: Result ->
                    when(result){
                        is Result.Failure -> EventManager.send(SnackBar(result.error?.message.toString()))
                        is Result.Success<*> -> {
                            saveUserInfo(result.data as AuthResult)
                            EventManager.send(PopBackStack)
                            EventManager.send(Navigate(Home))
                        }
                    }
                    EventManager.send(ProgressBar(false))
                }
            }
            else -> EventManager.send(event)
        }
    }

    private fun saveUserInfo(authResult: AuthResult) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = ServiceLocator.getUserRepository().getUserById(authResult.user?.uid.toString())
            ServiceLocator.getUserRepository().saveUser(user)
        }
    }

}