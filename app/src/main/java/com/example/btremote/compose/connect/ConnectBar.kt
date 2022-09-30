package com.example.btremote.compose.connect

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.example.btremote.connect.BluetoothStatus
import com.example.btremote.connect.WifiStatus
import com.example.btremote.tools.ToastUtil

@SuppressLint("MissingPermission")
@Composable
fun ConnectBar(
    onOpenBluetoothButtonClick: () -> Unit,
    onOpenWIFIButtonClick: () -> Unit,
    onOpenUSBButtonClick: () -> Unit,
    isDark: Boolean,//选择黑白图标
    context: Context = LocalContext.current,
) {
    val wifiName = App.wifiName

    val connectedBluetoothDeviceState = App.connectedBluetoothDevice

    val wifiState = App.wifiState

    val bluetoothStatus = App.bluetoothState
    Box(modifier = Modifier.height(50.dp), contentAlignment = Alignment.TopCenter) {
        IconButton(onClick = onOpenBluetoothButtonClick) {
            Icon(
                painter = painterResource(
                    id = when (bluetoothStatus) {
                        BluetoothStatus.DISABLE -> R.drawable.baseline_bluetooth_disabled_black_24dp
                        BluetoothStatus.DISCONNECTED -> R.drawable.baseline_bluetooth_black_24dp
                        BluetoothStatus.CONNECTED -> R.drawable.baseline_bluetooth_connected_black_24dp
                        else -> {
                            R.drawable.baseline_bluetooth_black_24dp
                        }
                    }
                ), contentDescription = null, tint = if (!isDark) Color.Black else Color.White
            )
        }
        if (bluetoothStatus == BluetoothStatus.CONNECTED) {
            connectedBluetoothDeviceState?.let {
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
//    Box(modifier = Modifier.height(50.dp), contentAlignment = Alignment.TopCenter) {
//        IconButton(onClick = {
//
//            when (wifiState) {
//                WifiStatus.DISABLE -> ToastUtil.toast(context, "请先打开WIFI")
//                WifiStatus.DISCONNECTED -> ToastUtil.toast(context, "请先连接WIFI")
//                else -> onOpenWIFIButtonClick()
//            }
//        }) {
//            Icon(
//                painter = painterResource(
//                    id = when (wifiState) {
//                        WifiStatus.DISABLE -> R.drawable.baseline_signal_wifi_off_black_24dp
//                        WifiStatus.DISCONNECTED -> R.drawable.baseline_signal_wifi_bad_black_24dp
//                        WifiStatus.CONNECTED_1 -> R.drawable.baseline_network_wifi_1_bar_black_24dp
//                        WifiStatus.CONNECTED_2 -> R.drawable.baseline_network_wifi_2_bar_black_24dp
//                        WifiStatus.CONNECTED_3 -> R.drawable.baseline_network_wifi_3_bar_black_24dp
//                        WifiStatus.CONNECTED_4 -> R.drawable.baseline_signal_wifi_4_bar_black_24dp
//                        else -> R.drawable.baseline_signal_wifi_off_black_24dp
//                    }
//                ), contentDescription = null, tint = if (!isDark) Color.Black else Color.White
//            )
//        }
//        if (wifiState == WifiStatus.CONNECTED_1 || wifiState == WifiStatus.CONNECTED_2
//            || wifiState == WifiStatus.CONNECTED_3 || wifiState == WifiStatus.CONNECTED_4
//        )
//            Text(
//                text = wifiName,
//                fontSize = 7.sp,
//                color = if (!isDark) Color.DarkGray else Color.White,
//                fontFamily = FontFamily.Monospace,
//                modifier = Modifier
//                    .offset(y = 35.dp)
//                    .width(50.dp)
//            )
//    }
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        IconButton(onClick = onOpenUSBButtonClick) {
//            Icon(
//                painter = painterResource(id = R.drawable.baseline_usb_black_24dp),
//                contentDescription = null, tint = if (!isDark) Color.Black else Color.White
//            )
//        }
//        Text(text = "", color = if (!isDark) Color.DarkGray else Color.White)
//    }
    Spacer(modifier = Modifier.width(10.dp))
}