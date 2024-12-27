package com.wiseowl.notifier.ui.addrule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseowl.notifier.data.ServiceLocator
import com.wiseowl.notifier.ui.addrule.component.LocationSelector
import com.wiseowl.notifier.ui.addrule.model.AddRuleEvent
import com.wiseowl.notifier.ui.addrule.model.AddRuleUIEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest


@Preview
@Composable
fun AddRuleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddRuleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is AddRuleUIEvent.SearchPlace -> {
                    val location = ServiceLocator.getLocationService().getCurrentLocation(context, Dispatchers.IO)
                    if (location != null) {
                        viewModel.onEvent(
                            AddRuleEvent.OnSuggestionUpdated(
                                ServiceLocator.getPlacesService().getNearbyPlacesForQuery(it.searchKey, location)
                            )
                        )
                    }
                }
            }
        }
    }
    LaunchedEffect(key1 = true) {
        ServiceLocator.getLocationService().getLocationUpdates(context, Dispatchers.IO).collectLatest {
            viewModel.onEvent(AddRuleEvent.OnCurrentLocationUpdate(it))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier,
                text = "Add Rule",
                fontSize = 52.sp,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = state.ruleName.value.toString(),
                shape = AbsoluteCutCornerShape(0.dp),
                singleLine = true,
                label = { Text(state.ruleName.label) },
                onValueChange = { viewModel.onEvent(AddRuleEvent.OnChangeRuleName(it)) },
                isError = !state.ruleName.error.isNullOrEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )

            OutlinedTextField(
                value = state.ruleDescription.value.toString(),
                shape = AbsoluteCutCornerShape(0.dp),
                singleLine = true,
                label = { Text(state.ruleDescription.label) },
                onValueChange = { viewModel.onEvent(AddRuleEvent.OnChangeRuleDescription(it)) },
                isError = !state.ruleDescription.error.isNullOrEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            LocationSelector(
                modifier = Modifier
                    .height(OutlinedTextFieldDefaults.MinHeight)
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = state.selectedPlaceName.value.toString(),
                label = state.selectedPlaceName.label,
                suggestions = state.suggestions,
                currentLocation = state.currentLocation,
                error = state.selectedPlaceName.error.toString(),
                onEvent = (viewModel)::onEvent
            )

            OutlinedTextField(
                value = state.ruleRadius.value.toString(),
                shape = AbsoluteCutCornerShape(0.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(state.ruleRadius.label) },
                onValueChange = { viewModel.onEvent(AddRuleEvent.OnChangeRuleRadius(it.toInt())) },
                isError = !state.ruleName.error.isNullOrEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            OutlinedTextField(
                value = state.ruleDelay.value.toString(),
                shape = AbsoluteCutCornerShape(0.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(state.ruleDelay.label) },
                onValueChange = { viewModel.onEvent(AddRuleEvent.OnChangeRuleDelay(it.toInt())) },
                isError = !state.ruleDelay.error.isNullOrEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = AbsoluteCutCornerShape(0.dp),
            onClick = { viewModel.onEvent(AddRuleEvent.CreateRule) }
        ) {
            Text(text = "Create")
        }
    }
}