package com.example.btremote.compose

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.app.BLUETOOTHStatus
import com.example.btremote.viewmodel.MainViewModel


@SuppressLint("MissingPermission")
@Composable
fun BluetoothList(paired: List<BluetoothDevice>, unpaired: List<BluetoothDevice>, openState: Boolean) {


    var clickDevice by remember {
        mutableStateOf<BluetoothDevice?>(null)
    }

    val bluetoothStatus = App.bluetoothStateFlow.collectAsState()

    val connectedBluetoothDevice = App.connectedBluetoothDevice.collectAsState()

    val recDataLength = App.bluetoothRecDataLengthFlow.collectAsState()

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        if (bluetoothStatus.value == BLUETOOTHStatus.CONNECTED && connectedBluetoothDevice.value != null) {
            Text(text = "已连接设备", modifier = Modifier.padding(16.dp), color = Color.LightGray, fontSize = 11.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 15.dp)
            ) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    Image(painter = painterResource(id = R.drawable.bluetooth), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(30.dp))
                    Column {
                        Text(text = connectedBluetoothDevice.value!!.name, fontSize = 15.sp)
                        Text(text = "接收数据长度: ${recDataLength.value}", fontSize = 9.sp)
                    }
                }
                Button(
                    modifier = Modifier
                        .height(30.dp)
                        .width(60.dp),
                    onClick = { App.bluetoothService.stopConnect() },
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xAfff0000),
                        //不可以点击的颜色
                        disabledBackgroundColor = Color.Gray)
                ) {
                    Text(text = "断开", fontSize = 11.sp)
                }

            }
        }
        Text(text = "已配对设备", modifier = Modifier.padding(16.dp), color = Color.LightGray, fontSize = 11.sp)
        if (openState)
            paired.forEach {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 25.dp)
                    .clickable {
                        App.bluetoothService
                            .startConnect(it, false)
                        clickDevice = it
                    }) {
                    Image(painter = painterResource(id = R.drawable.bluetooth), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(30.dp))
                    Column {
                        Text(text = it.name, fontSize = 15.sp)
                        if (clickDevice == it && bluetoothStatus.value == BLUETOOTHStatus.CONNECTING)
                            Text(text = "正在连接中……", fontSize = 9.sp)
                    }
                }
            }
        Text(text = "可用设备", modifier = Modifier.padding(16.dp), color = Color.LightGray, fontSize = 11.sp)
        if (openState)
            unpaired.forEach {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 25.dp)
                    .clickable {
                        App.bluetoothService
                            .startConnect(it, false)
                        clickDevice = it

                    }) {
                    Image(painter = painterResource(id = R.drawable.bluetooth), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(30.dp))
                    Column {
                        Text(text = it.name, fontSize = 15.sp)
                        if (clickDevice == it && bluetoothStatus.value == BLUETOOTHStatus.CONNECTING)
                            Text(text = "正在连接中……", fontSize = 9.sp)
                    }
                }


            }
    }
}