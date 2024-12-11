package com.wiseowl.notifier.ui.addrule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseowl.notifier.ui.common.component.CustomTextField

@Preview
@Composable
fun AddRuleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddRuleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            modifier = Modifier,
            text = "Add Rule",
            fontSize = 32.sp
        )
        CustomTextField(data = state.ruleDelay){  }
    }
}