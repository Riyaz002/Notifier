package com.wiseowl.notifier.ui.common.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

enum class Side(val value: Int) {
    TOP(0),
    RIGHT(1),
    BOTTOM(2),
    LEFT(3)
}

@Composable
fun MovingParticle(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color? = null,
    speed: Int = Random.nextInt(0, 65),
    shape: Shape = Shape.entries.random()
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    var startSide = Side.entries.random()
    var endSide = Side.entries.filter { it != startSide }.random()
    val startPosition = remember { mutableStateOf(Offset(Random.nextInt(0, screenWidth).toFloat(), Random.nextInt(0, screenHeight).toFloat())) }
    val endPosition = remember { mutableStateOf(getRandoPosition(startSide, size, screenWidth, screenHeight)) }
    val animatable = remember { Animatable(startPosition.value, Offset.VectorConverter) }
    val shape = remember { mutableStateOf(shape) }
    val randomColor = remember { mutableStateOf(color ?: Color(
        red = Random.nextInt(0, 255),
        green = Random.nextInt(0, 255),
        blue = Random.nextInt(0, 255),
        alpha = Random.nextInt(50, 255)
    )) }

    LaunchedEffect(key1 = startPosition, key2 = endPosition) {
        while(true){
            endPosition.value = getRandoPosition(endSide, size, screenWidth, screenHeight)
            animatable.animateTo(
                endPosition.value,
                animationSpec = tween(
                    durationMillis = (1000f * (100f/speed.toFloat())).toInt(),
                    easing = LinearEasing
                )
            )
            startSide = Side.entries.random()
            endSide = Side.entries.filter { it != startSide }.random()
            startPosition.value = getRandoPosition(startSide, size, screenWidth, screenHeight)
            if(color==null){
                randomColor.value = Color(
                    red = Random.nextInt(0, 255),
                    green = Random.nextInt(0, 255),
                    blue = Random.nextInt(0, 255),
                    alpha = Random.nextInt(50, 255)
                )
            }
            animatable.animateTo(
                startPosition.value,
                animationSpec = tween(
                    durationMillis = 0
                )
            )
        }
    }
    Box(modifier.fillMaxSize().background(Color.Transparent)) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(animatable.value.x.dp, animatable.value.y.dp)
                .shape(randomColor.value, shape = shape.value)
        )
    }
}

fun getRandoPosition(side: Side, size: Dp, screenWidth: Int, screenHeight: Int): Offset {
    var offset = Random.nextDouble(0.0, 1.0).toFloat()
    if (side == Side.TOP || side == Side.BOTTOM) offset *= screenWidth else offset *= screenHeight
    return when (side) {
        Side.TOP -> Offset(-size.value + offset, -size.value)
        Side.RIGHT -> Offset(screenWidth + size.value, -size.value + offset)
        Side.BOTTOM -> Offset(-size.value + offset, screenHeight + size.value)
        Side.LEFT -> Offset(-size.value, -size.value + offset)
    }
}