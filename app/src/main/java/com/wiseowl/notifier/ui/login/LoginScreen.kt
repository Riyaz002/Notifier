package com.wiseowl.notifier.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiseowl.notifier.ui.login.model.LoginEvent
import com.wiseowl.notifier.ui.login.model.LoginState
import com.wiseowl.notifier.ui.navigation.Home
import com.wiseowl.notifier.ui.navigation.Navigator
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Column(
        modifier = modifier
    ) {
        var state: LoginState by remember {
            mutableStateOf(LoginState())
        }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = true) {
            scope.launch {
                viewModel.state.collect { newState ->
                    state = newState
                    if(state.isUserLoggedIn) Navigator.navigate(Home)
                }
            }
        }

        TextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(LoginEvent.EditUserName(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        TextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(LoginEvent.EditPassword(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f, true))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { viewModel.onEvent(LoginEvent.Login(state.email, state.password)) }
        ) {
            Text(text = "Login")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { viewModel.onEvent(LoginEvent.Register(state.email, state.password)) }
        ) {
            Text(text = "Register")
        }
    }
}