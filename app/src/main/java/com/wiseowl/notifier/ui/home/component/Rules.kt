package com.wiseowl.notifier.ui.home.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.wiseowl.notifier.domain.model.Rule
import com.wiseowl.notifier.ui.addrule.model.AddRuleEvent
import com.wiseowl.notifier.ui.home.model.HomeEvent
import kotlin.reflect.KFunction1

@Composable
@Preview
fun Rules(modifier: Modifier = Modifier, rules: List<Rule> = listOf(), onEvent: (HomeEvent) -> Unit = {}) {
    val title = if(rules.isEmpty()) "Oops, you have no current running rules.\nPlease add them." else "Rules"
    Text(
        text = title,
        fontSize = 32.sp
    )

    LazyColumn {
        items(rules.size){
            Rule(rules[it], onEvent)
        }
    }
}