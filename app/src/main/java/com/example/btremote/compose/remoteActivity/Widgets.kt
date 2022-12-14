package com.example.btremote.compose.remoteActivity

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.R
import com.example.btremote.database.remoteWidget.RemoteWidget
import com.example.btremote.viewmodel.RemoteViewModel
import kotlin.experimental.and
import kotlin.math.roundToInt

@Composable
fun Widgets(widgets: List<RemoteWidget>) {
    val haptic = LocalHapticFeedback.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
    {
        widgets.forEach { widget ->
            val modifier = Modifier
                .graphicsLayer(
                    translationX = widget.offset.value.x,
                    translationY = widget.offset.value.y,
                    scaleX = widget.zoom.value,
                    scaleY = widget.zoom.value,
                    rotationZ = widget.rotate.value
                )
            when (widget.type) {
                WidgetType.ROCKER -> {
                    Rocker(
                        modifier = modifier
                            .size(170.dp),
                        backgroundSize = 150.dp,
                        rockerSize = 70.dp,
                        onXChange = { x -> widget.arg1 = x },
                        onYChange = { y -> widget.arg2 = y }
                    )
                }
                WidgetType.UPPER_BUTTON -> {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            widget.arg2 = widget.arg1
                        },
                        modifier = modifier.size(50.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.up),
                            contentDescription = null
                        )
                    }
                }
                WidgetType.LOWER_BUTTON -> {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            widget.arg2 = widget.arg1
                        },
                        modifier = modifier.size(50.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.under),
                            contentDescription = null
                        )
                    }
                }
                WidgetType.LEFT_BUTTON -> {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            widget.arg2 = widget.arg1
                        },
                        modifier = modifier.size(50.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.left),
                            contentDescription = null
                        )
                    }
                }
                WidgetType.RIGHT_BUTTON -> {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            widget.arg2 = widget.arg1
                        },
                        modifier = modifier.size(50.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.right),
                            contentDescription = null
                        )
                    }
                }
                WidgetType.XBOX_A -> {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            widget.arg2 = widget.arg1
                        },
                        modifier = modifier.size(50.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.xboxa),
                            contentDescription = null
                        )
                    }
                }
                WidgetType.XBOX_B -> {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            widget.arg2 = widget.arg1
                        },
                        modifier = modifier.size(50.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.xboxb),
                            contentDescription = null
                        )
                    }
                }
                WidgetType.XBOX_X -> {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            widget.arg2 = widget.arg1
                        },
                        modifier = modifier.size(50.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.xboxx),
                            contentDescription = null
                        )
                    }
                }
                WidgetType.XBOX_Y -> {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            widget.arg2 = widget.arg1
                        },
                        modifier = modifier.size(50.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.xboxy),
                            contentDescription = null
                        )
                    }
                }
                WidgetType.SLIDER -> {
                    var progress by remember {
                        mutableStateOf(0f)
                    }
                    Slider(
                        value = progress,
                        onValueChange = { value ->
                            progress = value
                        },
                        onValueChangeFinished = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            widget.arg2 = progress.toInt().toByte()
                        },
                        valueRange = 0f..widget.arg1.toFloat(),
                        steps = 1,
                        colors = SliderDefaults.colors(
                            inactiveTickColor = Color.Yellow,
                            activeTickColor = Color.Yellow,
                            thumbColor = Color.White,
                            inactiveTrackColor = Color(0xff3399ff),
                            activeTrackColor = Color(0xff3399ff)

                        ),
                        modifier = modifier.width(100.dp)
                    )
                }
                WidgetType.BUTTON -> {
                    Button(
                        onClick = {
                            Log.d("color",widget.color.toString())
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            widget.arg2 = widget.arg1
                        },
                        modifier = modifier
                            .width(60.dp)
                            .height(30.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color((widget.color!!)+0xff000000)
                        )
                    ) {

                    }
                }
            }
        }
    }

}