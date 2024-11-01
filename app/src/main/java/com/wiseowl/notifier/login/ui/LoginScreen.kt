package com.wiseowl.notifier.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.wiseowl.notifier.login.LoginViewModel
import com.wiseowl.notifier.login.model.LoginEvent

@Composable
fun LoginScreen(
    modifier: Modifier,
    viewModel: LoginViewModel
){
    Column(
        modifier = modifier
    ) {
        val state = viewModel.state.collectAsState()


        TextField(
            value = state.value.userName,
            onValueChange = { viewModel.onEvent(LoginEvent.EditUserName(it)) }
        )

        TextField(
            value = state.value.password,
            onValueChange = { viewModel.onEvent(LoginEvent.EditPassword(it)) }
        )

        Button(onClick = { viewModel.onEvent(LoginEvent.Login) }) {

        }
    }
}