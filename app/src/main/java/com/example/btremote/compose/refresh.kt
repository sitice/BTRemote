package com.example.btremote.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.min


@Composable
fun Refresh(modifier: Modifier = Modifier, color: Color, nowAngle: Float) {

    Canvas(modifier = modifier, onDraw = {
        val canvasWidth = size.width  // 画布的宽
        val canvasHeight = size.height  // 画布的高

        val strokeWidth = min(canvasWidth, canvasHeight) / 10
        val radius = (min(canvasWidth, canvasHeight) - strokeWidth) / 2
        drawCircle(color = Color.LightGray, alpha = 0.5f, style = Stroke(width = strokeWidth), radius = radius)
        drawArc(
            color = color,
            startAngle = nowAngle,
            useCenter = false,
            sweepAngle = 60f,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
            size = Size(radius * 2, radius * 2)
        )
    })
}

@Composable
fun RefreshAnimation(modifier: Modifier = Modifier, color: Color, isShow: MutableState<Boolean>) {

    if (isShow.value) {
        val state by remember {
            mutableStateOf(true)
        }
        val value by animateFloatAsState(
            targetValue = if (state) 360f else 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Restart
            )
        )
        Box(modifier = modifier) {
            Refresh(modifier, color, value)
        }
    }
}
