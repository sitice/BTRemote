package com.example.btremote.compose.remote

import android.icu.number.Scale
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.database.cmd.CMDSend
import com.example.btremote.database.viewpos.ViewPosAndGesture
import com.example.btremote.tools.EasyDataStore
import com.example.btremote.ui.theme.gradient3
import com.example.btremote.view.MyRockerView
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun RemoteCompose() {

    val list = listOf(
        "LeftRocker",
        "RightRocker",
        "XBoxA",
        "XBoxB",
        "XBoxX",
        "XBoxY",
        "Left",
        "Right",
        "Up",
        "Down"
    )

    val offsetMap = mutableMapOf<String, MutableState<Offset>>()
    val scaleMap = mutableMapOf<String, MutableState<Float>>()

    for (i in list) {
        val x = EasyDataStore.getSyncData("${i}_offset_x", 0f)
        val y = EasyDataStore.getSyncData("${i}_offset_y", 0f)
        val scale = EasyDataStore.getSyncData("${i}_scale", 1f)
        offsetMap[i] = remember {
            mutableStateOf(Offset(x, y))
        }
        scaleMap[i] = remember {
            mutableStateOf(scale)
        }
    }

    Box(
        Modifier
            .background(gradient3)
            .fillMaxSize()
    ) {
        AndroidView(modifier = Modifier
            .align(Alignment.BottomStart)
            .size(200.dp)
            .scale(scaleMap["LeftRocker"]!!.value)
            .offset {
                IntOffset(offsetMap["LeftRocker"]!!.value.x.roundToInt() + 100, offsetMap["LeftRocker"]!!.value.y.roundToInt() - 100)
            }
            .pointerInput(UInt) {
//                detectDragGestures(
//                    onDrag = { change, _offset ->
//                        offsetMap["LeftRocker"]!!.value += _offset
//                    })
            }, factory = { context ->
            MyRockerView(
                context,
                AppCompatResources.getDrawable(context, R.drawable.xuniyaogan),
                AppCompatResources.getDrawable(context, R.drawable.xuniyaogfan1)
            ).apply {
                var distanceLeft = 0
                setOnDistanceLevelListener {
                    distanceLeft = it
                }
                setOnAngleChangeListener(object : MyRockerView.OnAngleChangeListener {
                    override fun onStart() {

                    }

                    override fun angle(angle: Double) {
                        val radian = angle * PI / 180.0
                        val x = (cos(radian) * distanceLeft).toInt().toByte()
                        val y = (sin(radian) * distanceLeft).toInt().toByte()
                    }

                    override fun onFinish() {

                    }

                })
            }
        })
        AndroidView(modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(200.dp)
            .scale(scaleMap["RightRocker"]!!.value)
            .offset {
                IntOffset(offsetMap["RightRocker"]!!.value.x.roundToInt() - 100, offsetMap["RightRocker"]!!.value.y.roundToInt() - 100)
            }
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        offsetMap["RightRocker"]!!.value += _offset
                    })
            }, factory = { context ->
            MyRockerView(
                context,
                AppCompatResources.getDrawable(context, R.drawable.xuniyaogan),
                AppCompatResources.getDrawable(context, R.drawable.xuniyaogfan1)
            ).apply {
                var distanceLeft = 0
                setOnDistanceLevelListener {
                    distanceLeft = it
                }
                setOnAngleChangeListener(object : MyRockerView.OnAngleChangeListener {
                    override fun onStart() {

                    }

                    override fun angle(angle: Double) {
                        val radian = angle * PI / 180.0
                        val x = (cos(radian) * distanceLeft).toInt().toByte()
                        val y = (sin(radian) * distanceLeft).toInt().toByte()
                    }

                    override fun onFinish() {

                    }

                })
            }
        })
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .scale(scaleMap["XBoxA"]!!.value)
            .offset {
                IntOffset(offsetMap["XBoxA"]!!.value.x.roundToInt() - 750, offsetMap["XBoxA"]!!.value.y.roundToInt() - 150)
            }
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        offsetMap["XBoxA"]!!.value += _offset
                    })
            }) {
            Image(painter = painterResource(id = R.drawable.xboxa), contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .scale(scaleMap["XBoxB"]!!.value)
            .offset {
                IntOffset(offsetMap["XBoxB"]!!.value.x.roundToInt() - 600, offsetMap["XBoxB"]!!.value.y.roundToInt() - 300)
            }
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        offsetMap["XBoxB"]!!.value += _offset
                    })
            }) {
            Image(painter = painterResource(id = R.drawable.xboxb), contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .scale(scaleMap["XBoxX"]!!.value)
            .offset {
                IntOffset(offsetMap["XBoxX"]!!.value.x.roundToInt() - 900, offsetMap["XBoxX"]!!.value.y.roundToInt() - 300)
            }
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        offsetMap["XBoxX"]!!.value += _offset
                    })
            }) {
            Image(painter = painterResource(id = R.drawable.xboxx), contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .scale(scaleMap["XBoxY"]!!.value)
            .offset {
                IntOffset(offsetMap["XBoxY"]!!.value.x.roundToInt() - 750, offsetMap["XBoxY"]!!.value.y.roundToInt() - 450)
            }
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        offsetMap["XBoxY"]!!.value += _offset
                    })
            }) {
            Image(painter = painterResource(id = R.drawable.xboxy), contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .scale(scaleMap["Left"]!!.value)
            .offset {
                IntOffset(offsetMap["Left"]!!.value.x.roundToInt() + 750, offsetMap["Left"]!!.value.y.roundToInt() - 150)
            }
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        offsetMap["Left"]!!.value += _offset
                    },)
            }) {
            Image(painter = painterResource(id = R.drawable.left), contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .scale(scaleMap["Right"]!!.value)
            .offset {
                IntOffset(offsetMap["Right"]!!.value.x.roundToInt() + 600, offsetMap["Right"]!!.value.y.roundToInt() - 300)
            }
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        offsetMap["Right"]!!.value += _offset
                    },)
            }) {
            Image(painter = painterResource(id = R.drawable.right), contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .scale(scaleMap["Up"]!!.value)
            .offset {
                IntOffset(offsetMap["Up"]!!.value.x.roundToInt() + 900, offsetMap["Up"]!!.value.y.roundToInt() - 300)
            }
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        offsetMap["Up"]!!.value += _offset
                    },)
            }) {
            Image(painter = painterResource(id = R.drawable.up), contentDescription = null)
        }
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .scale(scaleMap["Down"]!!.value)
            .offset {
                IntOffset(offsetMap["Down"]!!.value.x.roundToInt() + 750, offsetMap["Down"]!!.value.y.roundToInt() - 450)
            }
            .pointerInput(UInt) {
                detectDragGestures(
                    onDrag = { change, _offset ->
                        offsetMap["Down"]!!.value += _offset
                    })
            }) {
            Image(painter = painterResource(id = R.drawable.under), contentDescription = null)
        }
    }
}


@Preview
@Composable
fun RemoteComposePreview() {
    RemoteCompose()
}