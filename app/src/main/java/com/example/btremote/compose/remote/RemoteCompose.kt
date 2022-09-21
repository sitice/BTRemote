package com.example.btremote.compose.remote

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
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
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.example.btremote.MainActivity
import com.example.btremote.Orientation
import com.example.btremote.R
import com.example.btremote.RemoteActivity
import com.example.btremote.app.App
import com.example.btremote.app.BLUETOOTHStatus
import com.example.btremote.compose.ConnectBar
import com.example.btremote.compose.ShowBluetoothDevice
import com.example.btremote.compose.ShowUSBDevice
import com.example.btremote.compose.ShowWifiDevice
import com.example.btremote.compose.rocker.Rocker
import com.example.btremote.lifecycle.REQUEST_ENABLE_BT
import com.example.btremote.protocol.buttonsList
import com.example.btremote.tools.EasyDataStore
import com.example.btremote.tools.EasyDataStore.dataStore1
import com.example.btremote.ui.theme.Blue
import com.example.btremote.ui.theme.gradient3
import com.example.btremote.viewmodel.RemoteViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RemoteCompose(context: Context = LocalContext.current, model: RemoteViewModel = viewModel()) {

    val offsetMap = mutableMapOf<String, MutableState<Offset>>()
    val scaleMap = mutableMapOf<String, MutableState<Float>>()
    val degreesMap = mutableMapOf<String, MutableState<Float>>()

    val preOffsetMap = mutableMapOf<String, Offset>()
    val preScaleMap = mutableMapOf<String, Float>()
    val preDegreesMap = mutableMapOf<String, Float>()

    EasyDataStore.dataStore = context.dataStore1
    for (i in buttonsList) {
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
        preOffsetMap[i] = Offset(x, y)
        preScaleMap[i] = scale
        preDegreesMap[i] = degrees
    }
    var gyroMode by remember {
        mutableStateOf(false)
    }

    val editModelState = remember {
        mutableStateOf(false)
    }

    var toolVisible by remember {
        mutableStateOf(false)
    }

    val openBluetoothDialog = remember {
        mutableStateOf(false)
    }

    val openWifiDialog = remember {
        mutableStateOf(false)
    }

    val openUSBDialog = remember {
        mutableStateOf(false)
    }

    val nowEditView = remember {
        mutableStateOf("")
    }


    val distanceX by model.distanceXFlow.collectAsState()
    val distanceY by model.distanceYFlow.collectAsState()
    val yaw by model.yawFlow.collectAsState()
    val voltage by model.voltageFlow.collectAsState()

    val addButtonAnimate = animateFloatAsState(
        targetValue = if (toolVisible) 45f else 0f,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = 0,
            easing = LinearEasing
        )
    )

    val orientation = Orientation.instance(context)

    val eulerAngle = orientation.angles.collectAsState()

    ShowBluetoothDevice(
        openDialog = openBluetoothDialog
    ) {
        if (it) {
            val bluetoothAdapter: BluetoothAdapter =
                App.bluetoothService.mManager.adapter
            if (!bluetoothAdapter.isEnabled) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                model.requestLauncher.launch(intent)
                val activity = context as RemoteActivity
                activity.setResult(REQUEST_ENABLE_BT)
            }
        } else {
            val bluetoothAdapter: BluetoothAdapter =
                App.bluetoothService.mManager.adapter
            if (bluetoothAdapter.isEnabled) {
                bluetoothAdapter.disable()
            }
        }
    }
    ShowWifiDevice(openDialog = openWifiDialog)
    ShowUSBDevice(openDialog = openUSBDialog)
    Box(
        Modifier
            .background(gradient3)
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = !editModelState.value,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier
                .align(Alignment.TopStart),
        )
        {
            Row(
                modifier = Modifier
                    .padding(start = 40.dp)
            )
            {
                ConnectBar(openBluetoothDialog, openWifiDialog, openUSBDialog, true)
            }
        }
        AnimatedVisibility(
            visible = !editModelState.value,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp, start = 5.dp)
            ) {
                IconButton(
                    onClick = { toolVisible = !toolVisible }, modifier = Modifier.rotate(
                        addButtonAnimate.value
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_circle_black_24dp),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                AnimatedVisibility(
                    visible = toolVisible,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .padding(top = 10.dp)
                                .size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_settings_black_24dp),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .padding(top = 10.dp)
                                .size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_bug_report_black_36dp),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {
                                editModelState.value = !editModelState.value
                                for (i in buttonsList) {
                                    preOffsetMap[i] = offsetMap[i]!!.value
                                    preDegreesMap[i] = degreesMap[i]!!.value
                                    preScaleMap[i] = scaleMap[i]!!.value
                                }
                            }, modifier = Modifier
                                .padding(top = 10.dp)
                                .size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_mode_edit_outline_black_24dp),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {
                                gyroMode = !gyroMode
                                if (gyroMode) {
                                    orientation.init()
                                } else {
                                    orientation.unregister()
                                }
                            }, modifier = Modifier
                                .padding(top = 10.dp)
                                .size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = if (gyroMode) R.drawable.gyro else R.drawable.baseline_games_black_24dp),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }


        AnimatedVisibility(
            visible = !editModelState.value,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier
                .align(Alignment.TopEnd),
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp)
            ) {
                AnimatedVisibility(
                    visible = gyroMode,
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(25.dp),
                            painter = painterResource(id = R.drawable.gyro),
                            contentDescription = null,
                            tint = Color.White
                        )
                        Text(
                            text = "pitch:", modifier = Modifier
                                .padding(start = 5.dp), color = Color.White, fontSize = 10.sp
                        )
                        Text(
                            text = String.format("%.1f", eulerAngle.value[0]), modifier = Modifier
                                .padding(start = 5.dp)
                                .width(30.dp), color = Color.White, fontSize = 10.sp
                        )
                        Text(
                            text = "roll:", modifier = Modifier
                                .padding(start = 0.dp), color = Color.White, fontSize = 10.sp
                        )
                        Text(
                            text = String.format("%.1f", eulerAngle.value[1]), modifier = Modifier
                                .padding(start = 5.dp)
                                .width(30.dp), color = Color.White, fontSize = 10.sp
                        )
                        Text(
                            text = "yaw:", modifier = Modifier
                                .padding(start = 0.dp), color = Color.White, fontSize = 10.sp
                        )
                        Text(
                            text = String.format("%.1f", eulerAngle.value[2]), modifier = Modifier
                                .padding(start = 5.dp)
                                .width(30.dp), color = Color.White, fontSize = 10.sp
                        )
                    }
                }
                Icon(
                    modifier = Modifier
                        .padding()
                        .size(20.dp),
                    painter = painterResource(id = R.drawable.distance),
                    contentDescription = null,
                    tint = Color.White
                )

                Text(
                    text = "x:", modifier = Modifier
                        .padding(start = 10.dp), color = Color.White, fontSize = 11.sp
                )
                Text(
                    text = distanceX ?: "null", modifier = Modifier
                        .padding(start = 10.dp)
                        .width(35.dp), color = Color.White, fontSize = 11.sp
                )
                Text(
                    text = "y:", modifier = Modifier
                        .padding(start = 0.dp), color = Color.White, fontSize = 11.sp
                )
                Text(
                    text = distanceY ?: "null", modifier = Modifier
                        .padding(start = 5.dp)
                        .width(35.dp), color = Color.White, fontSize = 11.sp
                )
                Icon(
                    modifier = Modifier
                        .padding()
                        .size(25.dp),
                    painter = painterResource(id = R.drawable.euler_yaw),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = "yaw:", modifier = Modifier
                        .padding(start = 5.dp), color = Color.White, fontSize = 11.sp
                )
                Text(
                    text = yaw ?: "null", modifier = Modifier
                        .padding(start = 10.dp)
                        .width(35.dp), color = Color.White, fontSize = 11.sp
                )

                Icon(
                    modifier = Modifier
                        .padding()
                        .size(20.dp),
                    painter = painterResource(id = R.drawable.vot),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = "voltage:", modifier = Modifier
                        .padding(start = 5.dp), color = Color.White, fontSize = 11.sp
                )
                Text(
                    text = voltage ?: "null", modifier = Modifier
                        .padding(start = 10.dp)
                        .width(35.dp), color = Color.White, fontSize = 11.sp
                )
            }

        }
        Buttons(editModelState, offsetMap, scaleMap, degreesMap, nowEditView, {
            editModelState.value = !editModelState.value
            nowEditView.value = ""
            for (i in buttonsList) {
                offsetMap[i]?.value = preOffsetMap[i]!!
                degreesMap[i]?.value = preDegreesMap[i]!!
                scaleMap[i]?.value = preScaleMap[i]!!
            }
        })
    }
}