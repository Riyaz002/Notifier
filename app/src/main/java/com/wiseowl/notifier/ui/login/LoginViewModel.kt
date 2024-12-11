package com.wiseowl.notifier.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.wiseowl.notifier.data.local.repository.UserRepositoryImpl
import com.wiseowl.notifier.data.ServiceLocator
import com.wiseowl.notifier.domain.event.ProgressBarEvent
import com.wiseowl.notifier.domain.util.Result
import com.wiseowl.notifier.domain.event.SnackBarEvent
import com.wiseowl.notifier.ui.login.model.LoginEvent
import com.wiseowl.notifier.ui.login.model.LoginState
import kotlinx.coroutines.Dispatchers
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
                    _state.update { loginState -> loginState.copy(email = loginState.email.copy(value = event.value)) }
                }
            }

            is LoginEvent.EditPassword -> {
                viewModelScope.launch {
                    _state.update{ loginState -> loginState.copy(password = loginState.password.copy(value = event.value)) }
                }
            }

            is LoginEvent.Login -> {
                ProgressBarEvent.send(true)
                ServiceLocator.getAuthenticator().signIn(event.email, event.password){ result: Result ->
                    when(result){
                        is Result.Failure -> SnackBarEvent.send(result.error?.message.toString())
                        is Result.Success<*> -> {
                            saveUserInfo(result.data as AuthResult)
                            _state.update{ loginState -> loginState.copy(isUserLoggedIn = true) }
                        }
                    }
                    ProgressBarEvent.send(false)
                }
            }
        }
    }

    private fun saveUserInfo(authResult: AuthResult) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = UserRepositoryImpl.getInstance().getUserById(authResult.user?.uid.toString())
            UserRepositoryImpl.getInstance().saveUser(user)
        }
    }

}