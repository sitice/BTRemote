package com.example.btremote.compose.connect

import android.content.Context
import androidx.compose.foundation.*
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
import com.example.btremote.tools.ToastUtil
import com.example.btremote.ui.theme.roundedCorner10dp

data class UartBond(var bond: Int)

@Composable
fun ShowUSBDevice(
    onOpenStateChange: () -> Unit,
    context: Context = LocalContext.current
) {
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
    val usbStatus = App.usbState
    val name = remember {
        mutableStateOf(App.mUSBService.getDeviceName())
    }
    Dialog(onDismissRequest = onOpenStateChange) {
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
                    text = name.value ?: "???????????????",
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700)
                )
                if (name.value == null)
                    Text(
                        text = "?????????????????????",
                        fontSize = 11.sp
                    )
                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                ) {
                    TextField(
                        // ?????????????????????
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent, // ??????????????????????????????
                            unfocusedIndicatorColor = Color.Transparent, // ??????????????????????????????
                            errorIndicatorColor = Color.Red, // ???????????????????????????
                            disabledIndicatorColor = Color.Gray // ??????????????????????????????
                        ),
                        isError = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = roundedCorner10dp,
                        textStyle = TextStyle(fontSize = 11.sp),
                        value = bond, // ????????????
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
                        }, // ??????????????????????????????text
                        label = @Composable {
                            Text(text = "?????????")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row {
                        Button(
                            onClick = onOpenStateChange,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = null,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                //????????????????????????
                                disabledBackgroundColor = Color.Gray
                            )
                        ) {
                            Text(text = "??????", color = Color.Black, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(30.dp))
                        Button(
                            enabled = name.value != null,
                            onClick = {
                                if (bond != "" && bond.length < 8) {
                                    App.mUSBService.open(bond.toInt())
                                    onOpenStateChange()
                                } else {
                                    ToastUtil.toast(context, "???????????????????????????")
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = null,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (!usbStatus) Color(0xff1a75ff) else Color(
                                    0xffff0040
                                ),
                                //????????????????????????
                                disabledBackgroundColor = Color.Gray
                            )
                        ) {
                            Text(
                                text = if (!usbStatus) "??????" else "??????",
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
