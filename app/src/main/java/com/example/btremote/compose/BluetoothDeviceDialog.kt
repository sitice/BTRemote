package com.example.btremote.compose

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.example.btremote.MainActivity
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.app.BLUETOOTHStatus
import com.example.btremote.lifecycle.REQUEST_ENABLE_BT
import com.example.btremote.tools.ToastUtil
import com.example.btremote.ui.theme.Blue
import com.example.btremote.viewmodel.MainViewModel


@SuppressLint("MissingPermission")
@Composable
fun ShowBluetoothDevice(openDialog: MutableState<Boolean>, context:Context = LocalContext.current) {
    val model:MainViewModel = viewModel()
    val switch = App.bluetoothStateFlow.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))
    val progress by animateLottieCompositionAsState(composition = composition,
        speed = 1f, // 加快播放速度（2倍速）
        iterations = LottieConstants.IterateForever // 设置永不结束
         )
    val scanState = App.bluetoothScanStateFlow.collectAsState()
    val pairedBluetoothDevices = App.pairedBluetoothFlow.collectAsState()
    val unpairedBluetoothDevices = App.unpairedBluetoothFlow.collectAsState()
    if (openDialog.value) {
        App.bluetoothService.startScan()
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(600.dp)
                ) {
                    Row(
                        Modifier
                            .padding(20.dp)
                            .height(60.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "蓝牙", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(20.dp))
                        Switch(
                            modifier = Modifier.padding(top = 2.dp),
                            checked = switch.value != BLUETOOTHStatus.DISABLE,
                            colors = SwitchDefaults.colors(checkedThumbColor = Blue),
                            onCheckedChange = {
                                if (it){
                                    val bluetoothAdapter: BluetoothAdapter = App.bluetoothService.mManager.adapter
                                    if (!bluetoothAdapter.isEnabled){
                                        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                                        model.requestLauncher.launch(intent)
                                        val activity = context as MainActivity
                                        activity.setResult(REQUEST_ENABLE_BT)
                                    }
                                }else{
                                    val bluetoothAdapter: BluetoothAdapter = App.bluetoothService.mManager.adapter
                                    if (bluetoothAdapter.isEnabled){
                                        bluetoothAdapter.disable()
                                    }
                                }
                            })
                        Spacer(modifier = Modifier.width(70.dp))
                        if (scanState.value)
                            LottieAnimation(composition = composition, progress = progress,Modifier.height(60.dp))

                    }
                    BluetoothList(pairedBluetoothDevices.value,unpairedBluetoothDevices.value ,switch.value != BLUETOOTHStatus.DISABLE)
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun ShowBluetoothDevicePreview()
//{
//    val a = remember {
//        mutableStateOf(true)
//    }
//    ShowBluetoothDevice(a)
//}