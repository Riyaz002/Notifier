package com.wiseowl.notifier.ui.common.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.position(x: Dp = 0.dp, y: Dp = 0.dp): Modifier {
    return this.graphicsLayer {
        translationX = x.toPx()
        translationY = y.toPx()
    }
}