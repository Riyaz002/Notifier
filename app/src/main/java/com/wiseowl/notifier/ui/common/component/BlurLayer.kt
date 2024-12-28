package com.wiseowl.notifier.ui.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun BlurLayer(
    modifier: Modifier = Modifier,
    blurRadius: Float,
    content: @Composable() (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifier.graphicsLayer {
            renderEffect = if (blurRadius > 0) {
                BlurEffect(
                    blurRadius,
                    blurRadius,
                    TileMode.Mirror
                )
            } else { null }
        },
        content = content
    )
}