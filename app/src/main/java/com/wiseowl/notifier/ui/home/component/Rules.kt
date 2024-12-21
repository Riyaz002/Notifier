package com.wiseowl.notifier.ui.home.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.wiseowl.notifier.domain.model.Rule
import com.wiseowl.notifier.ui.home.model.HomeEvent

@Composable
@Preview
fun Rules(modifier: Modifier = Modifier, rules: List<Rule> = listOf(), onEvent: (HomeEvent) -> Unit = {}) {
    val title2 = buildAnnotatedString {
        if(rules.isEmpty()) {
            withStyle(
                ParagraphStyle(
                    TextAlign.Center,
                    lineHeight = 1.em
                )
            ){
                withStyle(
                    SpanStyle(
                        fontSize = 52.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                ){ append("Oops\n") }
                withStyle(
                    SpanStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium
                    )
                ){ append("you have no current running rules.\nPlease add them.") }
            }
        } else{
            withStyle(
                SpanStyle(
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Medium
                )
            ){ append("Rules") }
        }
    }
    Text(
        text = title2,
        fontSize = 32.sp,
        textAlign = TextAlign.Center
    )

    LazyColumn {
        items(rules.size){
            Rule(rules[it], onEvent)
        }
    }
}