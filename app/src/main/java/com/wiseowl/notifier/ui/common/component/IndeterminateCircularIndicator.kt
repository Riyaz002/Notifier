package com.wiseowl.notifier.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IndeterminateCircularIndicator() {
    Box(
        Modifier.fillMaxSize()
            .clickable(false){}
    ) {
        Box(Modifier.fillMaxSize().alpha(0.2f).background(color = Color.Gray))
        CircularProgressIndicator(
            modifier = Modifier
                .width(32.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}