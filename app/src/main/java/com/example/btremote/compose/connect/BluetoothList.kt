package com.example.btremote.compose.connect

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
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.connect.BluetoothStatus

@SuppressLint("MissingPermission")
@Composable
fun BluetoothList() {

    var clickDevice by remember {
        mutableStateOf<BluetoothDevice?>(null)
    }

    val bluetoothStatus = App.bluetoothState

    val connectedBluetoothDevice = App.connectedBluetoothDevice

    val recDataLength = App.bluetoothRecDataLength

    val paired = App.bluetoothService.mManager.adapter.bondedDevices

    val unpaired = App.unpairedBluetooth

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        if (bluetoothStatus == BluetoothStatus.CONNECTED && connectedBluetoothDevice != null) {
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
                        Text(text = connectedBluetoothDevice.name, fontSize = 15.sp)
                        Text(text = "接收数据长度: $recDataLength", fontSize = 9.sp)
                    }
                }
                Button(
                    modifier = Modifier
                        .height(30.dp)
                        .width(60.dp),
                    onClick = { App.bluetoothService.stopConnect() },
                    shape = RoundedCornerShape(15.dp),
                    elevation = null,
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
        if (bluetoothStatus != BluetoothStatus.DISABLE)
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
                        Text(text = it.name?:"null", fontSize = 15.sp)
                        if (clickDevice == it && bluetoothStatus == BluetoothStatus.CONNECTING)
                            Text(text = "正在连接中……", fontSize = 9.sp)
                    }
                }
            }
        Text(text = "可用设备", modifier = Modifier.padding(16.dp), color = Color.LightGray, fontSize = 11.sp)
        if (bluetoothStatus != BluetoothStatus.DISABLE)
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
                        if (clickDevice == it && bluetoothStatus == BluetoothStatus.CONNECTING)
                            Text(text = "正在连接中……", fontSize = 9.sp)
                    }
                }
            }
    }
}