package com.wiseowl.notifier.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiseowl.notifier.login.model.LoginEvent
import com.wiseowl.notifier.login.model.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> get() = _state

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.EditUserName -> {
                viewModelScope.launch {
                    state.collect{ loginState ->
                        _state.value = loginState.copy(userName = event.value)
                    }
                }
            }

            is LoginEvent.EditPassword -> {
                viewModelScope.launch {
                    state.collect{ loginState ->
                        _state.value = loginState.copy(password = event.value)
                    }
                }
            }
            LoginEvent.Login -> {
                //TODO: Login
            }
        }
    }

}