package com.example.btremote.compose

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.btremote.viewmodel.MainViewModel


@SuppressLint("MissingPermission")
@Composable
fun BluetoothList(paired: List<BluetoothDevice>, unpaired: List<BluetoothDevice> , openState:Boolean) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(text = "已配对设备", modifier = Modifier.padding(16.dp), color = Color.LightGray, fontSize = 11.sp)
        if (openState)
            paired.forEach {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, bottom = 25.dp)
                    .clickable {
                        App.bluetoothService
                            .startConnect(it, false)
                    }) {
                    Image(painter = painterResource(id = R.drawable.bluetooth), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(text = it.name, fontSize = 15.sp)
                }
            }
        Text(text = "可用设备", modifier = Modifier.padding(16.dp), color = Color.LightGray, fontSize = 11.sp)
        if (openState)
            unpaired.forEach {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, bottom = 25.dp)
                    .clickable {
                        App.bluetoothService
                            .startConnect(it, false)
                    }) {
                    Image(painter = painterResource(id = R.drawable.bluetooth), contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(text = it.name, fontSize = 15.sp)
                }
            }
    }
}