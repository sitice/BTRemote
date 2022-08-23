package com.example.btremote.compose.remote

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.btremote.R
import com.example.btremote.compose.rocker.Rocker
import com.example.btremote.protocol.*
import com.example.btremote.tools.AppUtils
import com.example.btremote.tools.EasyDataStore
import com.example.btremote.tools.EasyDataStore.dataStore1
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Buttons(
    editModelState: MutableState<Boolean>,
    offsetMap: Map<String, MutableState<Offset>>,
    scaleMap: Map<String, MutableState<Float>>,
    degreesMap: Map<String, MutableState<Float>>,
    nowEditView: MutableState<String>,
    onClick: () -> Unit,
    context: Context = LocalContext.current
) {
    val progress = remember { mutableStateOf(0f) }
    val patter = longArrayOf(0,10)
    sliderVal = EasyDataStore.getSyncData("silder_progress", 0)

    Box(modifier = Modifier.fillMaxSize()) {

        AnimatedVisibility(
            visible = editModelState.value,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))

            ) {
                IconButton(onClick = {
                    if (nowEditView.value != "") {
                        degreesMap[nowEditView.value]!!.value -= 2
                    }
                }, modifier = Modifier.size(30.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_rotate_left_black_24dp),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        if (nowEditView.value != "") {
                            degreesMap[nowEditView.value]!!.value += 2
                        }
                    }, modifier = Modifier
                        .padding(start = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_rotate_right_black_24dp),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        if (nowEditView.value != "") {
                            scaleMap[nowEditView.value]!!.value *= 0.9f
                            if (scaleMap[nowEditView.value]!!.value <= 0.1) {
                                scaleMap[nowEditView.value]!!.value = 0.1f
                            }
                        }
                    }, modifier = Modifier
                        .padding(start = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_text_decrease_black_24dp),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        if (nowEditView.value != "") {
                            scaleMap[nowEditView.value]!!.value /= 0.9f
                            if (scaleMap[nowEditView.value]!!.value >= 10) {
                                scaleMap[nowEditView.value]!!.value = 10f
                            }
                        }
                    }, modifier = Modifier
                        .padding(start = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_text_increase_black_24dp),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        offsetMap.values.forEach {
                            it.value = Offset.Zero
                        }
                        scaleMap.values.forEach {
                            it.value = 1f
                        }
                        degreesMap.values.forEach {
                            it.value = 0f
                        }
                    }, modifier = Modifier
                        .padding(start = 10.dp)
                        .size(30.dp)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_settings_backup_restore_black_24dp),
                        contentDescription = null
                    )
                }
//                IconButton(
//                    onClick = { /*TODO*/ }, modifier = Modifier
//                        .padding(start = 10.dp)
//                        .size(30.dp)
//                ) {
//                    Icon(painter = painterResource(id = R.drawable.baseline_add_circle_black_24dp), contentDescription = null)
//                }
//                IconButton(
//                    onClick = { /*TODO*/ }, modifier = Modifier
//                        .padding(start = 10.dp)
//                        .size(30.dp)
//                ) {
//                    Icon(painter = painterResource(id = R.drawable.round_delete_forever_black_48dp), contentDescription = null)
//                }
                IconButton(
                    onClick = onClick, modifier = Modifier
                        .padding(start = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_cancel_black_24dp),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        editModelState.value = !editModelState.value
                        nowEditView.value = ""
                        EasyDataStore.dataStore = context.dataStore1
                        for (i in buttonsList) {
                            EasyDataStore.putSyncData("${i}_offset_x", offsetMap[i]!!.value.x)
                            EasyDataStore.putSyncData("${i}_offset_y", offsetMap[i]!!.value.y)
                            EasyDataStore.putSyncData("${i}_scale", scaleMap[i]!!.value)
                            EasyDataStore.putSyncData("${i}_degrees", degreesMap[i]!!.value)
                        }
                    }, modifier = Modifier
                        .padding(start = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_circle_black_24dp),
                        contentDescription = null
                    )
                }
            }
        }

        Slider(
            value = progress.value,
            onValueChange = {
                progress.value = it
            },
            onValueChangeFinished = {
                sliderVal = progress.value.toInt()
                EasyDataStore.putSyncData("silder_progress", sliderVal)
                AppUtils.instance?.vibratorForLollipop(context,patter)
            },
            enabled = !editModelState.value,
            valueRange = 0f..2f,
            steps = 1,
            colors = SliderDefaults.colors(
                inactiveTickColor = Color.Yellow,
                activeTickColor = Color.Yellow,
                thumbColor = Color.White,
                inactiveTrackColor = Color(0xff3399ff),
                activeTrackColor = Color(0xff3399ff)
            ),
            modifier = Modifier
                .width(100.dp)
                .align(Alignment.BottomCenter)
                .offset {
                    IntOffset(
                        offsetMap["Slider1"]!!.value.x.roundToInt(),
                        offsetMap["Slider1"]!!.value.y.roundToInt() - 50
                    )
                }
                .scale(scaleMap["Slider1"]!!.value)
                .rotate(degreesMap["Slider1"]!!.value)
                .border(
                    2.dp,
                    color = if (nowEditView.value == "Slider1") Color.Blue else Color.Transparent,
                    RoundedCornerShape(5.dp)
                )
                .pointerInput(UInt) {
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView.value == "Slider1")
                                offsetMap["Slider1"]!!.value += _offset
                        },
                        onDragStart = {
                            if (editModelState.value) {
                                nowEditView.value = "Slider1"
                            }
                        }
                    )

                }
        )

        Rocker(modifier = if (editModelState.value) Modifier
            .align(Alignment.BottomStart)
            .size(170.dp)
            .offset {
                IntOffset(
                    offsetMap["LeftRocker"]!!.value.x.roundToInt() + 100,
                    offsetMap["LeftRocker"]!!.value.y.roundToInt() - 100
                )
            }
            .scale(scaleMap["LeftRocker"]!!.value)
            .rotate(degreesMap["LeftRocker"]!!.value)
            .border(
                2.dp,
                color = if (nowEditView.value == "LeftRocker") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            )
            .clickable(enabled = nowEditView.value != "LeftRocker") {
                nowEditView.value = "LeftRocker"
            }
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        if (nowEditView.value == "LeftRocker")
                            offsetMap["LeftRocker"]!!.value += _offset
                    }
                )

            } else Modifier
            .align(Alignment.BottomStart)
            .size(170.dp)
            .offset {
                IntOffset(
                    offsetMap["LeftRocker"]!!.value.x.roundToInt() + 100,
                    offsetMap["LeftRocker"]!!.value.y.roundToInt() - 100
                )
            }
            .scale(scaleMap["LeftRocker"]!!.value)
            .rotate(degreesMap["LeftRocker"]!!.value)
            .border(
                2.dp,
                color = if (nowEditView.value == "LeftRocker") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            ), backgroundSize = 150.dp, rockerSize = 70.dp, editModelState, onDistanceChange = {
            leftDis = it
        }, onAngleChange = {
            leftAngle = it
        })
        Rocker(modifier = if (editModelState.value)
            Modifier
                .align(Alignment.BottomEnd)
                .size(170.dp)
                .offset {
                    IntOffset(
                        offsetMap["RightRocker"]!!.value.x.roundToInt() - 100,
                        offsetMap["RightRocker"]!!.value.y.roundToInt() - 100
                    )
                }
                .scale(scaleMap["RightRocker"]!!.value)
                .rotate(degreesMap["RightRocker"]!!.value)
                .border(
                    2.dp,
                    color = if (nowEditView.value == "RightRocker") Color.Blue else Color.Transparent,
                    RoundedCornerShape(5.dp)
                )
                .clickable(enabled = nowEditView.value != "RightRocker") {
                    nowEditView.value = "RightRocker"
                }
                .pointerInput(UInt) {
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView.value == "RightRocker")
                                offsetMap["RightRocker"]!!.value += _offset
                        }
                    )

                } else Modifier
            .align(Alignment.BottomEnd)
            .size(170.dp)
            .offset {
                IntOffset(
                    offsetMap["RightRocker"]!!.value.x.roundToInt() - 100,
                    offsetMap["RightRocker"]!!.value.y.roundToInt() - 100
                )
            }
            .scale(scaleMap["RightRocker"]!!.value)
            .rotate(degreesMap["RightRocker"]!!.value)
            .border(
                2.dp,
                color = if (nowEditView.value == "RightRocker") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            ),
            backgroundSize = 150.dp,
            rockerSize = 70.dp,
            editModelState,
            onDistanceChange = { rightDis = it },
            onAngleChange = { rightAngle = it })
        IconButton(onClick = {
            if (editModelState.value)
                nowEditView.value = "XBoxA"
            else {
                keyVal = keyValMap["XBoxA"]!!
                AppUtils.instance?.vibratorForLollipop(context,patter)
            }
        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .offset {
                IntOffset(
                    offsetMap["XBoxA"]!!.value.x.roundToInt() - 750,
                    offsetMap["XBoxA"]!!.value.y.roundToInt() - 150
                )
            }
            .scale(scaleMap["XBoxA"]!!.value)
            .rotate(degreesMap["XBoxA"]!!.value)
            .border(
                2.dp,
                color = if (nowEditView.value == "XBoxA") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            )
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        if (nowEditView.value == "XBoxA")
                            offsetMap["XBoxA"]!!.value += _offset
                    })
            }) {
            Image(painter = painterResource(id = R.drawable.xboxa), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState.value)
                nowEditView.value = "XBoxB"
            else {
                keyVal = keyValMap["XBoxB"]!!
                AppUtils.instance?.vibratorForLollipop(context,patter)
            }

        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .rotate(degreesMap["XBoxB"]!!.value)
            .scale(scaleMap["XBoxB"]!!.value)
            .offset {
                IntOffset(
                    offsetMap["XBoxB"]!!.value.x.roundToInt() - 600,
                    offsetMap["XBoxB"]!!.value.y.roundToInt() - 300
                )
            }
            .border(
                2.dp,
                color = if (nowEditView.value == "XBoxB") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            )
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        if (nowEditView.value == "XBoxB")
                            offsetMap["XBoxB"]!!.value += _offset
                    })
            }) {
            Image(painter = painterResource(id = R.drawable.xboxb), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState.value)
                nowEditView.value = "XBoxX"
            else {
                keyVal = keyValMap["XBoxX"]!!
                AppUtils.instance?.vibratorForLollipop(context,patter)
            }
        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .offset {
                IntOffset(
                    offsetMap["XBoxX"]!!.value.x.roundToInt() - 900,
                    offsetMap["XBoxX"]!!.value.y.roundToInt() - 300
                )
            }
            .scale(scaleMap["XBoxX"]!!.value)
            .rotate(degreesMap["XBoxX"]!!.value)
            .border(
                2.dp,
                color = if (nowEditView.value == "XBoxX") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            )
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        if (nowEditView.value == "XBoxX")
                            offsetMap["XBoxX"]!!.value += _offset
                    }
                )
            }) {
            Image(painter = painterResource(id = R.drawable.xboxx), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState.value)
                nowEditView.value = "XBoxY"
            else {
                keyVal = keyValMap["XBoxY"]!!
                AppUtils.instance?.vibratorForLollipop(context,patter)
            }
        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .offset {
                IntOffset(
                    offsetMap["XBoxY"]!!.value.x.roundToInt() - 750,
                    offsetMap["XBoxY"]!!.value.y.roundToInt() - 450
                )
            }
            .scale(scaleMap["XBoxY"]!!.value)
            .rotate(degreesMap["XBoxY"]!!.value)
            .border(
                2.dp,
                color = if (nowEditView.value == "XBoxY") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            )
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        if (nowEditView.value == "XBoxY")
                            offsetMap["XBoxY"]!!.value += _offset
                    }
                )
            }) {
            Image(painter = painterResource(id = R.drawable.xboxy), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState.value)
                nowEditView.value = "Left"
            else {
                keyVal = keyValMap["Left"]!!
                AppUtils.instance?.vibratorForLollipop(context,patter)
            }
        }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .offset {
                IntOffset(
                    offsetMap["Left"]!!.value.x.roundToInt() + 600,
                    offsetMap["Left"]!!.value.y.roundToInt() - 300
                )
            }
            .scale(scaleMap["Left"]!!.value)
            .rotate(degreesMap["Left"]!!.value)
            .border(
                2.dp,
                color = if (nowEditView.value == "Left") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            )
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        if (nowEditView.value == "Left")
                            offsetMap["Left"]!!.value += _offset
                    }
                )
            }) {
            Image(painter = painterResource(id = R.drawable.left), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState.value)
                nowEditView.value = "Right"
            else {
                keyVal = keyValMap["Right"]!!
                AppUtils.instance?.vibratorForLollipop(context,patter)
            }
        }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .offset {
                IntOffset(
                    offsetMap["Right"]!!.value.x.roundToInt() + 900,
                    offsetMap["Right"]!!.value.y.roundToInt() - 300
                )
            }
            .scale(scaleMap["Right"]!!.value)
            .rotate(degreesMap["Right"]!!.value)
            .border(
                2.dp,
                color = if (nowEditView.value == "Right") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            )
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        if (nowEditView.value == "Right")
                            offsetMap["Right"]!!.value += _offset
                    }
                )
            }) {
            Image(painter = painterResource(id = R.drawable.right), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState.value)
                nowEditView.value = "Up"
            else {
                keyVal = keyValMap["Up"]!!
                AppUtils.instance?.vibratorForLollipop(context,patter)
            }
        }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .offset {
                IntOffset(
                    offsetMap["Up"]!!.value.x.roundToInt() + 750,
                    offsetMap["Up"]!!.value.y.roundToInt() - 450
                )
            }
            .scale(scaleMap["Up"]!!.value)
            .rotate(degreesMap["Up"]!!.value)
            .border(
                2.dp,
                color = if (nowEditView.value == "Up") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            )
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        if (nowEditView.value == "Up")
                            offsetMap["Up"]!!.value += _offset
                    }
                )
            }) {
            Image(painter = painterResource(id = R.drawable.up), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState.value)
                nowEditView.value = "Down"
            else {
                keyVal = keyValMap["Down"]!!
                AppUtils.instance?.vibratorForLollipop(context,patter)
            }
        }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .offset {
                IntOffset(
                    offsetMap["Down"]!!.value.x.roundToInt() + 750,
                    offsetMap["Down"]!!.value.y.roundToInt() - 150
                )
            }
            .scale(scaleMap["Down"]!!.value)
            .rotate(degreesMap["Down"]!!.value)
            .border(
                2.dp,
                color = if (nowEditView.value == "Down") Color.Blue else Color.Transparent,
                RoundedCornerShape(5.dp)
            )
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        if (nowEditView.value == "Down")
                            offsetMap["Down"]!!.value += _offset
                    })
            }) {
            Image(painter = painterResource(id = R.drawable.under), contentDescription = null)
        }
        Button(
            onClick = {
                if (editModelState.value)
                    nowEditView.value = "Blue"
                else {
                    keyVal = keyValMap["Blue"]!!
                    AppUtils.instance?.vibratorForLollipop(context,patter)
                }
            },
            modifier = Modifier
                .width(60.dp)
                .height(30.dp)
                .align(Alignment.Center)
                .offset {
                    IntOffset(
                        offsetMap["Blue"]!!.value.x.roundToInt() + 100,
                        offsetMap["Blue"]!!.value.y.roundToInt()
                    )
                }
                .scale(scaleMap["Blue"]!!.value)
                .rotate(degreesMap["Blue"]!!.value)
                .border(
                    2.dp,
                    color = if (nowEditView.value == "Blue") Color.Blue else Color.Transparent,
                    RoundedCornerShape(5.dp)
                )
                .pointerInput(UInt) {
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView.value == "Blue")
                                offsetMap["Blue"]!!.value += _offset
                        })
                },
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xff1a75ff),
                //不可以点击的颜色
                disabledBackgroundColor = Color.Gray
            )
        ) {}
        Button(
            onClick = {
                if (editModelState.value)
                    nowEditView.value = "Yellow"
                else {
                    keyVal = keyValMap["Yellow"]!!
                    AppUtils.instance?.vibratorForLollipop(context,patter)
                }
            },
            modifier = Modifier
                .width(60.dp)
                .height(30.dp)
                .align(Alignment.Center)
                .offset {
                    IntOffset(
                        offsetMap["Yellow"]!!.value.x.roundToInt() - 100,
                        offsetMap["Yellow"]!!.value.y.roundToInt()
                    )
                }
                .scale(scaleMap["Yellow"]!!.value)
                .rotate(degreesMap["Yellow"]!!.value)
                .border(
                    2.dp,
                    color = if (nowEditView.value == "Yellow") Color.Blue else Color.Transparent,
                    RoundedCornerShape(5.dp)
                )
                .pointerInput(UInt) {
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView.value == "Yellow")
                                offsetMap["Yellow"]!!.value += _offset
                        })
                },
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xffffdf00),
                //不可以点击的颜色
                disabledBackgroundColor = Color.Gray
            )
        ) {}
    }
}