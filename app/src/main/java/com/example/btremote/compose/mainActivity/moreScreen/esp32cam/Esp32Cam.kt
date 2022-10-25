package com.example.btremote.compose.mainActivity.moreScreen.esp32cam

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.ui.theme.roundedCorner10dp
import com.example.btremote.connect.Esp32cam
import com.example.btremote.viewmodel.MainViewModel

@Composable
fun Esp32Cam(context: Context = LocalContext.current, model: MainViewModel = viewModel()) {
    model.esp32cam = Esp32cam(context, handler = model.handler)

    val url by model.esp32camIp.collectAsState(initial = "192.168.92.6")
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
                value = url,
                onValueChange = {
                    model.changeEsp32camIp(it)
                },
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
                    if (!model.esp32camState)
                        model.esp32cam.connect(url)
                    else
                        model.esp32cam.disconnect()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (!model.esp32camState) Color(0xff1a75ff) else Color(
                        0xffff1744
                    ),
                    //不可以点击的颜色
                    disabledBackgroundColor = Color.Gray
                )
            ) {
                Text(
                    text = if (!model.esp32camState) "打开" else "关闭",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        model.bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
        }
    }
}