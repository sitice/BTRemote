package com.example.btremote.compose.remoteActivity

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.database.remoteWidget.RemoteWidget
import com.example.btremote.tools.ToastUtil
import com.example.btremote.ui.theme.roundedCorner10dp
import com.example.btremote.viewmodel.MainViewModel
import com.example.btremote.viewmodel.RemoteViewModel
import kotlinx.coroutines.flow.map

@Composable
fun NewWidgetDialog(
    widgetType: Int,
    color: Int,
    onOpenColorSelectClick:()->Unit,
    onCancelClick: () -> Unit,
    onSureClick: (name: String, key: Byte, len: Byte) -> Unit,
    model: RemoteViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    Dialog(onDismissRequest = onCancelClick) {
        Surface(
            shape = RoundedCornerShape(20.dp)
        ) {
            var name by remember {
                mutableStateOf("")
            }

            var key by remember {
                mutableStateOf("")
            }

            var len by remember {
                mutableStateOf("")
            }

            var nameError by remember {
                mutableStateOf(false)
            }

            var keyError by remember {
                mutableStateOf(false)
            }
            var lenError by remember {
                mutableStateOf(false)
            }

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "请输入控件信息",
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                        unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                        errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                        disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                    ),
                    isError = nameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = roundedCorner10dp,
                    textStyle = TextStyle(fontSize = 11.sp),
                    value = name, // 显示文本
                    onValueChange = {
                        name = it
                        nameError = false
                        model.widgets.map { list ->
                            list.forEach { widget ->
                                Log.d("1","1")
                                if (name == widget.name) {
                                    nameError = true
                                    ToastUtil.toast(context, "名字重复")
                                    return@map
                                }
                            }
                        }
                    }, // 文字改变时，就赋值给text
                    label = @Composable {
                        Text(text = "名字")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )
                if (widgetType != WidgetType.ROCKER && widgetType != WidgetType.SLIDER) {
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                            unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                            errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                            disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                        ),
                        isError = keyError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = roundedCorner10dp,
                        textStyle = TextStyle(fontSize = 11.sp),
                        value = key, // 显示文本
                        onValueChange = {
                            key = it
                            keyError = false
                            if (key.toInt() > 255) {
                                keyError = true
                                ToastUtil.toast(context, "键值取值错误")
                            }
                        }, // 文字改变时，就赋值给text
                        label = @Composable {
                            Text(text = "键值 0~255")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
                if (widgetType == WidgetType.BUTTON) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onOpenColorSelectClick,
                            modifier = Modifier
                                .height(40.dp)
                                .width(120.dp),
                            elevation = null,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White
                            ),
                            border = BorderStroke(width = 2.dp, color = Color(0xff7e80f8))
                        ) {
                            Text(text = "点击选择颜色", fontSize = 11.sp)
                        }
                        Spacer(
                            modifier = Modifier
                                .height(30.dp)
                                .width(60.dp)
                                .background(color = Color(color))
                        )
                    }
                }
                if (widgetType == WidgetType.SLIDER) {
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                            unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                            errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                            disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                        ),
                        isError = lenError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = roundedCorner10dp,
                        textStyle = TextStyle(fontSize = 11.sp),
                        value = len, // 显示文本
                        onValueChange = {
                            len = it
                            lenError = false
                            if (len.toInt() > 10 || len.toInt() < 2) {
                                lenError = true
                                ToastUtil.toast(context, "请输入正确的长度")
                            }
                        }, // 文字改变时，就赋值给text
                        label = @Composable {
                            Text(text = "滑块长度 2~10")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Button(
                        onClick = onCancelClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0x201a75ff),
                            //不可以点击的颜色
                            disabledBackgroundColor = Color.Gray
                        )
                    ) {
                        Text(text = "取消", color = Color(0xff1a75ff), fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Button(
                        onClick = {
                            if (name != "" && !nameError) {
                                if (!keyError) {
                                    if (!lenError) {
                                        onSureClick(
                                            name,
                                            if (key == "") 0 else key.toByte(),
                                            if (len == "") 0 else len.toByte()
                                        )
                                    } else {
                                        ToastUtil.toast(context, "长度错误")
                                    }
                                } else {
                                    ToastUtil.toast(context, "键值错误")
                                }
                            } else {
                                ToastUtil.toast(context, "请输入正确的名字")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0x201a75ff),
                            //不可以点击的颜色
                            disabledBackgroundColor = Color.Gray
                        )
                    ) {
                        Text(
                            text = "确认",
                            color = Color(0xff1a75ff),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}