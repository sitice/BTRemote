package com.example.btremote.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.app.App
import com.example.btremote.tools.EasyDataStore
import com.example.btremote.tools.EasyDataStore.dataStore1

@Composable
fun Setting(context: Context = LocalContext.current) {
    EasyDataStore.readSendRecType(context)

    val screenList = listOf("基本收发", "高级收发", "波形显示", "遥控")
    val list = listOf("Bluetooth", "WIFI", "USB")
    val send = listOf("发送", "接收")

    Column(
        modifier = Modifier
            .background(Color.White)

            .padding(30.dp)
            .fillMaxSize()
    ) {
        Text(text = "设置", fontSize = 25.sp, fontWeight = FontWeight(700))
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "连接", fontSize = 11.sp, color = Color.Gray)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp)
        )
        screenList.forEach { screen ->
            Text(text = screen, fontSize = 20.sp, fontWeight = FontWeight(500))
            send.forEach { send ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = send)
                    Spacer(modifier = Modifier.width(20.dp))
                    var now by remember {
                        mutableStateOf(
                            if (send == "发送") {
                                when (screen) {
                                    "基本收发" -> {
                                        App.baseSendType.value
                                    }
                                    "高级收发" -> {
                                        App.advanceSendType.value
                                    }
                                    "波形显示" -> {
                                        App.waveSendType.value
                                    }
                                    "遥控" -> {
                                        App.remoteSendType.value
                                    }
                                    else -> {App.baseSendType.value}
                                }
                            } else {
                                when (screen) {
                                    "基本收发" -> {
                                        App.baseRecType.value
                                    }
                                    "高级收发" -> {
                                        App.advanceRecType.value
                                    }
                                    "波形显示" -> {
                                        App.waveRecType.value
                                    }
                                    "遥控" -> {
                                        App.remoteRecType.value
                                    }
                                    else -> {App.baseRecType.value }
                                }
                            }
                        )
                    }
                    list.forEach {
                        Text(text = it)
                        RadioButton(selected = now == it, onClick = {
                            if (send == "发送") {
                                when (screen) {
                                    "基本收发" -> {
                                        App.baseSendType.value = it
                                        EasyDataStore.putSyncData("baseSendType", it)
                                    }
                                    "高级收发" -> {
                                        App.advanceSendType.value = it
                                        EasyDataStore.putSyncData("advanceSendType", it)
                                    }
                                    "波形显示" -> {
                                        App.waveSendType.value = it
                                        EasyDataStore.putSyncData("waveSendType", it)
                                    }
                                    "遥控" -> {
                                        App.remoteSendType.value = it
                                        EasyDataStore.putSyncData("remoteSendType", it)
                                    }
                                }
                            } else {
                                when (screen) {
                                    "基本收发" -> {
                                        App.baseRecType.value = it
                                        EasyDataStore.putSyncData("baseRecType", it)
                                    }
                                    "高级收发" -> {
                                        App.advanceRecType.value = it
                                        EasyDataStore.putSyncData("advanceRecType", it)
                                    }
                                    "波形显示" -> {
                                        App.waveRecType.value = it
                                        EasyDataStore.putSyncData("waveRecType", it)
                                    }
                                    "遥控" -> {
                                        App.remoteRecType.value = it
                                        EasyDataStore.putSyncData("remoteRecType", it)
                                    }
                                }
                            }
                            now = it
                        })

                    }
                }
            }
        }


    }
}

@Composable
@Preview
fun SettingPreview() {
    Setting()
}
