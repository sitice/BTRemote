package com.example.btremote.compose

import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alibaba.fastjson.JSON
import com.example.btremote.app.App
import com.example.btremote.app.BLUETOOTHStatus
import com.example.btremote.compose.baseSendRec.Cmd
import com.example.btremote.protocol.string2Byte
import com.example.btremote.tools.SaveDataToLocalFile
import com.example.btremote.tools.ToastUtil
import com.example.btremote.ui.theme.roundedCorner10dp

data class UartBond(var bond: Int)

@Composable
fun ShowUSBDevice(openDialog: MutableState<Boolean>, context: Context = LocalContext.current) {
    if (openDialog.value) {
        var uartBond = UartBond(115200)
        val data = SaveDataToLocalFile.load(context, "UartBond")
        if (data == null) {
            SaveDataToLocalFile.save(context, "UartBond", JSON.toJSONString(uartBond))
        } else {
            uartBond = JSON.parseObject(data, UartBond::class.java) as UartBond
        }
        var bond by remember {
            mutableStateOf(uartBond.bond.toString())
        }
        val usbStatus = App.usbStateFlow.collectAsState()
        val name = remember {
            mutableStateOf(App.mUSBService.getDeviceName())
        }
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(210.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = name.value ?: "无设备连接",
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700)
                    )
                    if (name.value == null)
                        Text(
                            text = "请插入串口设备",
                            fontSize = 11.sp
                        )
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                    ) {
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
                            value = bond, // 显示文本
                            onValueChange = {
                                bond = it
                                if (bond != "" && bond.length < 8) {
                                    uartBond.bond = bond.toInt()
                                    SaveDataToLocalFile.save(
                                        context,
                                        "UartBond",
                                        JSON.toJSONString(uartBond)
                                    )
                                }
                            }, // 文字改变时，就赋值给text
                            label = @Composable {
                                Text(text = "波特率")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row {
                            Button(
                                onClick = { openDialog.value = false },
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
                                enabled = name.value != null,
                                onClick = {
                                    if (bond != "" && bond.length < 8) {
                                        App.mUSBService.open(bond.toInt())
                                        openDialog.value = false
                                    } else {
                                        ToastUtil.toast(context, "请输入正确的波特率")
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                elevation = null,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = if (!usbStatus.value) Color(0xff1a75ff) else Color(
                                        0xffff0040
                                    ),
                                    //不可以点击的颜色
                                    disabledBackgroundColor = Color.Gray
                                )
                            ) {
                                Text(
                                    text = if (!usbStatus.value) "打开" else "关闭",
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
