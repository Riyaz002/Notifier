package com.wiseowl.notifier.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseowl.notifier.ui.Navigate
import com.wiseowl.notifier.ui.home.component.Rules
import com.wiseowl.notifier.ui.home.model.HomeState
import com.wiseowl.notifier.ui.navigation.AddRule

@Preview
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state = viewModel.state.collectAsState(initial = HomeState())

    Box(modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier,
                text = buildAnnotatedString {
                    append("Welcome\n")
                    withStyle(style = SpanStyle(fontSize = 72.sp)){
                        append(state.value.user?.firstName)
                    }
                },
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Rules(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                rules = state.value.rules,
                onEvent = viewModel::onEvent
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            onClick = { viewModel.onEvent(Navigate(AddRule)) }
        ) { Icon(imageVector = Icons.Default.Add, contentDescription = "Add Rule") }
    }
}