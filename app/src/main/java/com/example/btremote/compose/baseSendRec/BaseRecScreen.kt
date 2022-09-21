package com.example.btremote.compose.baseSendRec

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.protocol.string2Byte
import com.example.btremote.tools.EasyDataStore
import com.example.btremote.ui.theme.roundedCorner10dp
import com.example.btremote.viewmodel.MainViewModel

@Composable
fun BaseRecScreen() {
    val model: MainViewModel = viewModel()
    var sendType by remember {
        mutableStateOf(EasyDataStore.getSyncData("Base_send_type",0))
    }

    // 定义一个可观测的text，用来在TextField中展示
    var text by remember {
        mutableStateOf("")
    }
    Column {
        Row(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    sendType =
                        if (sendType == HEX_DATA) STRING_DATA else HEX_DATA
                    EasyDataStore.putSyncData("Base_send_type",sendType)
                },
                modifier = Modifier
                    .width(80.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xA05e71f6),
                    contentColor = Color.Black
                ),
                elevation = null
            ) {
                Icon(
                    painter = painterResource(id = if (sendType == STRING_DATA) R.drawable.s else R.drawable.h),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
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
                    .padding(start = 20.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = roundedCorner10dp,
                textStyle = TextStyle(fontSize = 11.sp),
                value = text, // 显示文本
                onValueChange = { text = it }, // 文字改变时，就赋值给text
                label = { Text(text = "Input") }, // label是Input
                trailingIcon = @Composable {
                    Image(
                        painter = painterResource(id = R.drawable.send), // 搜索图标
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                val data =
                                    if (sendType == STRING_DATA) text.toByteArray() else string2Byte(
                                        text
                                    )
                                if (App.baseSendType.value == "Bluetooth") {
                                    App.bluetoothService.writeData(data)
                                } else if (App.baseSendType.value == "WIFI") {
                                    App.wifiService.write(data)
                                } else {
                                    App.mUSBService.write(data)
                                }

                            }) // 给图标添加点击事件，点击就吐司提示内容
                },
                leadingIcon = @Composable {
                    Image(imageVector = Icons.Filled.Clear, // 清除图标
                        contentDescription = null,
                        modifier = Modifier.clickable { text = "" }) // 给图标添加点击事件，点击就清空text
                },
                // placeholder = @Composable { Text(text = "This is placeholder") }, // 不输入内容时的占位符
            )
        }

    }

}