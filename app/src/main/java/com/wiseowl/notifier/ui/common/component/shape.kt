package com.wiseowl.notifier.ui.common.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

enum class Shape {
    CIRCLE,
    TRIANGLE,
    SQUARE,
    DIAMOND
}

fun Modifier.shape(color: Color, shape: Shape): Modifier {
    return this.drawBehind {
        // Define the triangle points
        when (shape) {
            Shape.CIRCLE -> drawCircle(color)
            Shape.SQUARE -> {
                val path = Path().apply {
                    moveTo(0f,0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(path, color)
            }
            Shape.TRIANGLE -> {
                val path = Path().apply {
                    moveTo(size.width / 2, 0f) // Top-center
                    lineTo(0f, size.height)   // Bottom-left
                    lineTo(size.width, size.height) // Bottom-right
                    close() // Close the path to form the triangle
                }
                drawPath(
                    path = path,
                    color = color
                )
            }

            Shape.DIAMOND -> {
                val path = Path().apply {
                    moveTo(size.width / 2, 0f)
                    lineTo(size.width, size.height/2)
                    lineTo(size.width/2, size.height)
                    lineTo(0f, size.height/2)
                    close()
                }
                drawPath(
                    path = path,
                    color = color
                )
            }
        }
    }
}