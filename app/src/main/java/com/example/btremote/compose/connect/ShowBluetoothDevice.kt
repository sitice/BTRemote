package com.example.btremote.compose.connect

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.*
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.connect.BluetoothStatus
import com.example.btremote.ui.theme.Blue


@SuppressLint("MissingPermission")
@Composable
fun ShowBluetoothDevice(
    onDismissRequest: () -> Unit,
    onCheckedChange: (check: Boolean) -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        speed = 1f, // 加快播放速度（2倍速）
        iterations = LottieConstants.IterateForever // 设置永不结束
    )
    val switch = App.bluetoothState
    val scanState = App.bluetoothScanning

    Dialog(onDismissRequest = onDismissRequest) {
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
                        .height(60.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "蓝牙", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(20.dp))
                    Switch(
                        modifier = Modifier.padding(top = 2.dp),
                        checked = switch != BluetoothStatus.DISABLE,
                        colors = SwitchDefaults.colors(checkedThumbColor = Blue),
                        onCheckedChange = onCheckedChange
                    )
                    Spacer(modifier = Modifier.width(70.dp))
                    if (scanState)
                        LottieAnimation(
                            composition = composition,
                            progress = progress,
                            Modifier.height(60.dp)
                        )

                }
                BluetoothList()
            }
        }
    }

}
