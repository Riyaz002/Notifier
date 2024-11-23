package com.wiseowl.notifier.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    Row(modifier.background(Color.Red)) {
        Text(text = "Home", style = TextStyle(color = Color.Black, fontSize = 32.sp))
    }
}