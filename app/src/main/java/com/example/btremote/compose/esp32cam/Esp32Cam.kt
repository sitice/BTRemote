package com.example.btremote.compose.esp32cam

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.ui.theme.roundedCorner10dp
import com.example.btremote.wifi.Esp32cam

@Composable
fun Esp32Cam() {
    val esp = Esp32cam()
    val cam = esp.bitmapFlow.collectAsState()

    var url by remember {
        mutableStateOf("192.168.50.26:80")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = "请将手机和ESP32CAM处于同一局域网下",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row {
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
                    .weight(4f)
                    .height(50.dp),
                shape = roundedCorner10dp,
                textStyle = TextStyle(fontSize = 11.sp),
                value = url, // 显示文本
                onValueChange = {
                    url = it
                }, // 文字改变时，就赋值给text
                label = @Composable {
                    Text(text = "url")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri
                )
            )
            Spacer(modifier = Modifier.width(20.dp))
            Button(
                onClick = {
                    esp.connect(url)
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
                    fontSize = 15.sp
                )
            }
        }

    }
    if (cam.value != null) {
        Image(bitmap = cam.value!!.asImageBitmap(), contentDescription = null)
    }

}