package com.example.btremote.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.alibaba.fastjson.JSON
import com.example.btremote.app.App
import com.example.btremote.tools.SaveDataToLocalFile
import com.example.btremote.ui.theme.roundedCorner10dp


data class WifiDeviceInfo(var ip: String, var port: String)

@Composable
fun ShowWifiDevice(openDialog: MutableState<Boolean>, context: Context = LocalContext.current) {
    if (openDialog.value) {

        val udpClient = "udpClient"
        val udpServer = "udpServer"
        val tcpClient = "tcpClient"
        val tcpServer = "tcpServer"
        var nowProtocol by remember {
            mutableStateOf(tcpClient)
        }
        var wifiDeviceInfo = WifiDeviceInfo("192.168.4.1", "333")
        val data = SaveDataToLocalFile.load(context, "WifiDeviceInfo")
        if (data == null) {
            SaveDataToLocalFile.save(context, "WifiDeviceInfo", JSON.toJSONString(wifiDeviceInfo))
        } else {
            wifiDeviceInfo = JSON.parseObject(data, WifiDeviceInfo::class.java) as WifiDeviceInfo
        }
        var ip by remember {
            mutableStateOf(wifiDeviceInfo.ip)
        }
        var port by remember {
            mutableStateOf(wifiDeviceInfo.port)
        }
        val name = App.wifiNameFlow.collectAsState()
        Dialog(onDismissRequest = {
            SaveDataToLocalFile.save(context, "WifiDeviceInfo", JSON.toJSONString(wifiDeviceInfo))
            openDialog.value = false
        }) {
            Surface(
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(320.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = name.value,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(60.dp)
                                    .clickable { nowProtocol = tcpClient},
                                shape = RoundedCornerShape(15.dp),
                                color = if (nowProtocol == tcpClient) Color(0xff1a75ff) else Color.Gray
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = tcpClient, fontSize = 10.sp, color = Color.White)
                                }

                            }
                            Surface(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(60.dp)
                                    .clickable {nowProtocol = tcpServer },
                                shape = RoundedCornerShape(15.dp),
                                color = if (nowProtocol == tcpServer) Color(0xff1a75ff) else Color.Gray
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = tcpServer, fontSize = 10.sp, color = Color.White)
                                }
                            }
                            Surface(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(60.dp)
                                    .clickable { nowProtocol = udpClient},
                                shape = RoundedCornerShape(15.dp),
                                color = if (nowProtocol == udpClient) Color(0xff1a75ff) else Color.Gray
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = udpClient, fontSize = 10.sp, color = Color.White)
                                }
                            }
                            Surface(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(60.dp)
                                    .clickable { nowProtocol = udpServer},
                                shape = RoundedCornerShape(15.dp),
                                color = if (nowProtocol == udpServer) Color(0xff1a75ff) else Color.Gray
                            ) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = udpServer, fontSize = 10.sp, color = Color.White)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(
                            // 指定下划线颜色
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                                unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                                errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                                disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                            ),
                            isError = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = roundedCorner10dp,
                            textStyle = TextStyle(fontSize = 11.sp),
                            value = ip, // 显示文本
                            onValueChange = {
                                ip = it
                                wifiDeviceInfo.ip = it
                            }, // 文字改变时，就赋值给text
                            label = @Composable {
                                Text(text = "IP")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Uri
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(
                            // 指定下划线颜色
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                                unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                                errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                                disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                            ),
                            isError = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = roundedCorner10dp,
                            textStyle = TextStyle(fontSize = 11.sp),
                            value = port, // 显示文本
                            onValueChange = {
                                port = it
                                wifiDeviceInfo.port = it
                            }, // 文字改变时，就赋值给text
                            label = @Composable {
                                Text(text = "端口")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row {
                            Button(
                                onClick = {
                                    SaveDataToLocalFile.save(
                                        context,
                                        "WifiDeviceInfo",
                                        JSON.toJSONString(wifiDeviceInfo)
                                    )
                                    openDialog.value = false
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                elevation = null,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Transparent,
                                    //不可以点击的颜色
                                    disabledBackgroundColor = Color.Gray
                                )
                            ) {
                                Text(text = "取消", color = Color.Black, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.width(30.dp))
                            Button(
                                onClick = {
                                    SaveDataToLocalFile.save(
                                        context,
                                        "WifiDeviceInfo",
                                        JSON.toJSONString(wifiDeviceInfo)
                                    )
                                    App.wifiService.startTcpClientConnect(ip, port.toInt())
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                elevation = null,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xff1a75ff),
                                    //不可以点击的颜色
                                    disabledBackgroundColor = Color.Gray
                                )
                            ) {
                                Text(
                                    text = "打开",
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
