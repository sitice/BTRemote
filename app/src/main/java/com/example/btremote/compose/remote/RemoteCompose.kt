package com.example.btremote.compose.remote

import android.icu.number.Scale
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.btremote.tools.LogUtil
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
    val degreesMap = mutableMapOf<String, MutableState<Float>>()
    for (i in list) {
        val x = EasyDataStore.getSyncData("${i}_offset_x", 0f)
        val y = EasyDataStore.getSyncData("${i}_offset_y", 0f)
        val scale = EasyDataStore.getSyncData("${i}_scale", 1f)
        val degrees = EasyDataStore.getSyncData("${i}_degrees", 0f)
        offsetMap[i] = remember {
            mutableStateOf(Offset(x, y))
        }
        scaleMap[i] = remember {
            mutableStateOf(scale)
        }
        degreesMap[i] = remember {
            mutableStateOf(degrees)
        }
    }

    val editModelState by remember {
        mutableStateOf(true)
    }

    var nowEditView by remember {
        mutableStateOf("")
    }

    var visible by remember {
        mutableStateOf(false)
    }

    Box(
        Modifier
            .background(gradient3)
            .fillMaxSize()
    ) {

        Row {
            IconButton(
                onClick = { visible = !visible }, modifier = Modifier
            ) {
                Icon(painter = painterResource(id = R.drawable.baseline_settings_black_24dp), contentDescription = null)
            }
            AnimatedVisibility(
                visible = visible,
            ) {
                Row {
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(30.dp)) {
                        Icon(painter = painterResource(id = R.drawable.baseline_settings_black_24dp), contentDescription = null)
                    }
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(30.dp)) {
                        Icon(painter = painterResource(id = R.drawable.baseline_settings_black_24dp), contentDescription = null)
                    }
                    IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(30.dp)) {
                        Icon(painter = painterResource(id = R.drawable.baseline_settings_black_24dp), contentDescription = null)
                    }
                }

            }
        }


        AndroidView(modifier = Modifier
            .align(Alignment.BottomStart)
            .size(170.dp)
            .scale(scaleMap["LeftRocker"]!!.value)
            .rotate(degreesMap["LeftRocker"]!!.value)
            .offset {
                IntOffset(offsetMap["LeftRocker"]!!.value.x.roundToInt() + 100, offsetMap["LeftRocker"]!!.value.y.roundToInt() - 100)
            }
            .border(2.dp, color = if (nowEditView == "LeftRocker") Color.Blue else Color.Transparent, RoundedCornerShape(5.dp))
            .pointerInput(UInt) {
                if (editModelState && nowEditView == "LeftRocker")
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            offsetMap["LeftRocker"]!!.value += _offset
                        },
                        onDragStart = {

                        }
                    )
                if (editModelState)
                    detectTapGestures {
                        nowEditView = "LeftRocker"
                    }
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
            .size(170.dp)
            .scale(scaleMap["RightRocker"]!!.value)
            .rotate(degreesMap["RightRocker"]!!.value)
            .offset {
                IntOffset(offsetMap["RightRocker"]!!.value.x.roundToInt() - 100, offsetMap["RightRocker"]!!.value.y.roundToInt() - 100)
            }
            .border(2.dp, color = if (nowEditView == "RightRocker") Color.Blue else Color.Transparent, RoundedCornerShape(5.dp))
            .pointerInput(UInt) {
                if (editModelState && nowEditView == "RightRocker")
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            offsetMap["RightRocker"]!!.value += _offset
                        },
                        onDragStart = {

                        }
                    )
                if (editModelState)
                    detectTapGestures {
                        nowEditView = "RightRocker"
                    }
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
        IconButton(onClick = {
            if (editModelState)
                nowEditView = "XBoxA"
        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .scale(scaleMap["XBoxA"]!!.value)
            .rotate(degreesMap["XBoxA"]!!.value)
            .offset {
                IntOffset(offsetMap["XBoxA"]!!.value.x.roundToInt() - 750, offsetMap["XBoxA"]!!.value.y.roundToInt() - 150)
            }
            .border(2.dp, color = if (nowEditView == "XBoxA") Color.Blue else Color.Transparent, RoundedCornerShape(5.dp))
            .pointerInput(UInt) {
                if (editModelState)
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView == "XBoxA")
                                offsetMap["XBoxA"]!!.value += _offset
                        })
            }) {
            Image(painter = painterResource(id = R.drawable.xboxa), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState)
                nowEditView = "XBoxB"
        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .scale(scaleMap["XBoxB"]!!.value)
            .rotate(degreesMap["XBoxB"]!!.value)
            .offset {
                IntOffset(offsetMap["XBoxB"]!!.value.x.roundToInt() - 600, offsetMap["XBoxB"]!!.value.y.roundToInt() - 300)
            }
            .border(2.dp, color = if (nowEditView == "XBoxB") Color.Blue else Color.Transparent, RoundedCornerShape(5.dp))
            .pointerInput(UInt) {
                if (editModelState)
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView == "XBoxB")
                                offsetMap["XBoxB"]!!.value += _offset
                        })
            }) {
            Image(painter = painterResource(id = R.drawable.xboxb), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState)
                nowEditView = "XBoxX"
        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .scale(scaleMap["XBoxX"]!!.value)
            .rotate(degreesMap["XBoxX"]!!.value)
            .offset {
                IntOffset(offsetMap["XBoxX"]!!.value.x.roundToInt() - 900, offsetMap["XBoxX"]!!.value.y.roundToInt() - 300)
            }
            .border(2.dp, color = if (nowEditView == "XBoxX") Color.Blue else Color.Transparent, RoundedCornerShape(5.dp))
            .pointerInput(UInt) {
                if (editModelState)
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView == "XBoxX")
                                offsetMap["XBoxX"]!!.value += _offset
                        }
                    )
            }) {
            Image(painter = painterResource(id = R.drawable.xboxx), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState)
                nowEditView = "XBoxY"
        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(50.dp)
            .scale(scaleMap["XBoxY"]!!.value)
            .rotate(degreesMap["XBoxY"]!!.value)
            .offset {
                IntOffset(offsetMap["XBoxY"]!!.value.x.roundToInt() - 750, offsetMap["XBoxY"]!!.value.y.roundToInt() - 450)
            }
            .border(2.dp, color = if (nowEditView == "XBoxY") Color.Blue else Color.Transparent, RoundedCornerShape(5.dp))
            .pointerInput(UInt) {
                if (editModelState)
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView == "XBoxY")
                                offsetMap["XBoxY"]!!.value += _offset
                        }
                    )
            }) {
            Image(painter = painterResource(id = R.drawable.xboxy), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState)
                nowEditView = "Left"
        }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .scale(scaleMap["Left"]!!.value)
            .rotate(degreesMap["Left"]!!.value)
            .offset {
                IntOffset(offsetMap["Left"]!!.value.x.roundToInt() + 600, offsetMap["Left"]!!.value.y.roundToInt() - 300)
            }
            .border(2.dp, color = if (nowEditView == "Left") Color.Blue else Color.Transparent, RoundedCornerShape(5.dp))
            .pointerInput(UInt) {
                if (editModelState)
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView == "Left")
                                offsetMap["Left"]!!.value += _offset
                        }
                    )
            }) {
            Image(painter = painterResource(id = R.drawable.left), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState)
                nowEditView = "Right"
        }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .scale(scaleMap["Right"]!!.value)
            .rotate(degreesMap["Right"]!!.value)
            .offset {
                IntOffset(offsetMap["Right"]!!.value.x.roundToInt() + 900, offsetMap["Right"]!!.value.y.roundToInt() - 300)
            }
            .border(2.dp, color = if (nowEditView == "Right") Color.Blue else Color.Transparent, RoundedCornerShape(5.dp))
            .pointerInput(UInt) {
                if (editModelState)
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView == "Right")
                                offsetMap["Right"]!!.value += _offset
                        }
                    )
            }) {
            Image(painter = painterResource(id = R.drawable.right), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState)
                nowEditView = "Up"
        }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .scale(scaleMap["Up"]!!.value)
            .rotate(degreesMap["Up"]!!.value)
            .offset {
                IntOffset(offsetMap["Up"]!!.value.x.roundToInt() + 750, offsetMap["Up"]!!.value.y.roundToInt() - 450)
            }
            .border(2.dp, color = if (nowEditView == "Up") Color.Blue else Color.Transparent, RoundedCornerShape(5.dp))
            .pointerInput(UInt) {
                if (editModelState)
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView == "Up")
                                offsetMap["Up"]!!.value += _offset
                        }
                    )
            }) {
            Image(painter = painterResource(id = R.drawable.up), contentDescription = null)
        }
        IconButton(onClick = {
            if (editModelState)
                nowEditView = "Down"
        }, modifier = Modifier
            .align(Alignment.BottomStart)
            .size(50.dp)
            .scale(scaleMap["Down"]!!.value)
            .rotate(degreesMap["Down"]!!.value)
            .offset {
                IntOffset(offsetMap["Down"]!!.value.x.roundToInt() + 750, offsetMap["Down"]!!.value.y.roundToInt() - 150)
            }
            .border(2.dp, color = if (nowEditView == "Down") Color.Blue else Color.Transparent, RoundedCornerShape(5.dp))
            .pointerInput(UInt) {
                if (editModelState)
                    detectDragGestures(
                        onDrag = { change, _offset ->
                            if (nowEditView == "Down")
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