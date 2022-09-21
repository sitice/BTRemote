package com.example.btremote.compose

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.app.BLUETOOTHStatus
import com.example.btremote.app.WIFIStatus
import com.example.btremote.tools.ToastUtil

@Composable
fun ConnectBar(
    openBluetoothDialog: MutableState<Boolean>,
    openWifiDialog: MutableState<Boolean>,
    openUSBDialog: MutableState<Boolean>,
    isDark: Boolean,//选择黑白图标
    context: Context = LocalContext.current
) {
    val wifiName = App.wifiNameFlow.collectAsState()

    val connectedBluetoothDeviceState = App.connectedBluetoothDevice.collectAsState()

    val wifiStatus = App.wifiStatusFlow.collectAsState()

    val bluetoothStatus = App.bluetoothStateFlow.collectAsState()
    Box(modifier = Modifier.height(50.dp), contentAlignment = Alignment.TopCenter) {
        IconButton(onClick = { openBluetoothDialog.value = true }) {
            Icon(
                painter = painterResource(
                    id = when (bluetoothStatus.value) {
                        BLUETOOTHStatus.DISABLE -> R.drawable.baseline_bluetooth_disabled_black_24dp
                        BLUETOOTHStatus.DISCONNECTED -> R.drawable.baseline_bluetooth_black_24dp
                        BLUETOOTHStatus.CONNECTED -> R.drawable.baseline_bluetooth_connected_black_24dp
                        else -> {
                            R.drawable.baseline_bluetooth_black_24dp
                        }
                    }
                ), contentDescription = null, tint = if (!isDark) Color.Black else Color.White
            )
        }
        if (bluetoothStatus.value == BLUETOOTHStatus.CONNECTED) {
            connectedBluetoothDeviceState.value?.let {
                Text(
                    text = it.name,
                    fontSize = 7.sp,
                    color = if (!isDark) Color.DarkGray else Color.White,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.offset(y = 35.dp)
                )
            }
        }
    }
    Box(modifier = Modifier.height(50.dp), contentAlignment = Alignment.TopCenter) {
        IconButton(onClick = {
            if (wifiStatus.value == WIFIStatus.DISABLE) {
                ToastUtil.toast(context, "请先打开WIFI")
            } else if (wifiStatus.value == WIFIStatus.DISCONNECTED) {
                ToastUtil.toast(context, "请先连接WIFI")
            } else {
                openWifiDialog.value = true
            }
        }) {
            Icon(
                painter = painterResource(
                    id = when (wifiStatus.value) {
                        WIFIStatus.DISABLE -> R.drawable.baseline_signal_wifi_off_black_24dp
                        WIFIStatus.DISCONNECTED -> R.drawable.baseline_signal_wifi_bad_black_24dp
                        WIFIStatus.CONNECTED_1 -> R.drawable.baseline_network_wifi_1_bar_black_24dp
                        WIFIStatus.CONNECTED_2 -> R.drawable.baseline_network_wifi_2_bar_black_24dp
                        WIFIStatus.CONNECTED_3 -> R.drawable.baseline_network_wifi_3_bar_black_24dp
                        WIFIStatus.CONNECTED_4 -> R.drawable.baseline_signal_wifi_4_bar_black_24dp
                        else -> R.drawable.baseline_signal_wifi_off_black_24dp
                    }
                ), contentDescription = null, tint = if (!isDark) Color.Black else Color.White
            )
        }
        if (wifiStatus.value == WIFIStatus.CONNECTED_1 || wifiStatus.value == WIFIStatus.CONNECTED_2
            || wifiStatus.value == WIFIStatus.CONNECTED_3 || wifiStatus.value == WIFIStatus.CONNECTED_4
        )
            Text(
                text = wifiName.value,
                fontSize = 7.sp,
                color = if (!isDark) Color.DarkGray else Color.White,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .offset(y = 35.dp)
                    .width(50.dp)
            )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { openUSBDialog.value = true }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_usb_black_24dp),
                contentDescription = null, tint = if (!isDark) Color.Black else Color.White
            )
        }
        Text(text = "", color = if (!isDark) Color.DarkGray else Color.White)
    }
    Spacer(modifier = Modifier.width(10.dp))
}