package com.wiseowl.notifier.ui.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wiseowl.notifier.ui.registration.model.RegistrationEvent
import com.wiseowl.notifier.ui.registration.model.RegistrationState
import com.wiseowl.notifier.ui.navigation.Home
import com.wiseowl.notifier.ui.navigation.Navigate
import com.wiseowl.notifier.ui.navigation.Navigator
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        val state by viewModel.state.collectAsState()

        OutlinedTextField(
            value = state.firstName.value.toString(),
            label = { Text(text = state.firstName.label)},
            shape = AbsoluteCutCornerShape(0.dp),
            onValueChange = { viewModel.onEvent(RegistrationEvent.EditFirstName(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        OutlinedTextField(
            value = state.lastName.value.toString(),
            label = { Text(text = state.lastName.label)},
            shape = AbsoluteCutCornerShape(0.dp),
            onValueChange = { viewModel.onEvent(RegistrationEvent.EditLastName(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )


        OutlinedTextField(
            value = state.email.value.toString(),
            label = { Text(text = state.email.label)},
            shape = AbsoluteCutCornerShape(0.dp),
            onValueChange = { viewModel.onEvent(RegistrationEvent.EditEmail(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        OutlinedTextField(
            value = state.password.value.toString(),
            label = { Text(text = state.password.label)},
            shape = AbsoluteCutCornerShape(0.dp),
            onValueChange = { viewModel.onEvent(RegistrationEvent.EditPassword(it)) },
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
            onClick = { viewModel.onEvent(RegistrationEvent.Register) }
        ) { Text(text = "Create Account") }
    }
}