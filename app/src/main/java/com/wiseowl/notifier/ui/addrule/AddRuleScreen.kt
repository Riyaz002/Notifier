package com.wiseowl.notifier.ui.addrule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.Place
import com.wiseowl.notifier.ui.addrule.model.AddRuleEvent

@Preview
@Composable
fun AddRuleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddRuleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Column {
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

            OutlinedTextField(
                value = state.place.value.toString(),
                shape = AbsoluteCutCornerShape(0.dp),
                singleLine = true,
                label = { Text(state.place.label) },
                onValueChange = {
                    viewModel.onEvent(
                        AddRuleEvent.OnChangeRulePlace(
                            Place(
                                "Place",
                                Location(0.0, 0.0)
                            )
                        )
                    )
                },
                isError = !state.place.error.isNullOrEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
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