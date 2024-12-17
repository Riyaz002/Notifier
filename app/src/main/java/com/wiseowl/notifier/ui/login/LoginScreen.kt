package com.wiseowl.notifier.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiseowl.notifier.ui.login.model.LoginEvent
import com.wiseowl.notifier.ui.login.model.LoginState
import com.wiseowl.notifier.ui.navigation.Home
import com.wiseowl.notifier.ui.navigation.Navigate
import com.wiseowl.notifier.ui.navigation.Navigator
import com.wiseowl.notifier.ui.navigation.Registration
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        val state by viewModel.state.collectAsState()

        OutlinedTextField(
            value = state.email.value.toString(),
            shape = AbsoluteCutCornerShape(0.dp),
            singleLine = true,
            label = { Text(state.email.label)},
            onValueChange = { viewModel.onEvent(LoginEvent.EditUserName(it)) },
            isError = !state.email.error.isNullOrEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        OutlinedTextField(
            value = state.password.value.toString(),
            shape = AbsoluteCutCornerShape(0.dp),
            singleLine = true,
            label = { Text(text = state.password.label)},
            onValueChange = { viewModel.onEvent(LoginEvent.EditPassword(it)) },
            isError = !state.password.error.isNullOrEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = AbsoluteCutCornerShape(0.dp)
                ),
            onClick = { viewModel.onEvent(LoginEvent.Login(state.email.value.toString(), state.password.value.toString())) }
        ) {
            Text(text = "Login")
        }

        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { Navigator.navigate(Navigate(Registration)) }
        ) { Text(text = "Don't have an account? Create one") }
    }
}