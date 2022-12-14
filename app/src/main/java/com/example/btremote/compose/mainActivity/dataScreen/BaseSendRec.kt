package com.example.btremote.compose.mainActivity.dataScreen

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
import com.example.btremote.connect.DataFormat
import com.example.btremote.protocol.string2Byte
import com.example.btremote.ui.theme.roundedCorner10dp
import com.example.btremote.viewmodel.MainViewModel

@Composable
fun BaseSendRec(model: MainViewModel = viewModel()) {
    val baseSendType by model.baseSendType.collectAsState(initial = 0)
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
                    if (baseSendType == DataFormat.HEX) {
                        model.changeBaseSendType(DataFormat.STR)
                    } else {
                        model.changeBaseSendType(DataFormat.HEX)
                    }
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
                    painter = painterResource(id = if (baseSendType == DataFormat.STR) R.drawable.s else R.drawable.h),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
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
                    .padding(start = 20.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = roundedCorner10dp,
                textStyle = TextStyle(fontSize = 11.sp),
                value = model.baseSendText, // ????????????
                onValueChange = { model.baseSendText = it }, // ??????????????????????????????text
                label = { Text(text = "Input") }, // label???Input
                trailingIcon = @Composable {
                    Image(
                        painter = painterResource(id = R.drawable.send), // ????????????
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                val data =
                                    if (baseSendType == DataFormat.HEX) model.baseSendText.toByteArray() else string2Byte(
                                        model.baseSendText
                                    )
                                App.bluetoothService.writeData(data)
                            }) // ?????????????????????????????????????????????????????????
                },
                leadingIcon = @Composable {
                    Image(imageVector = Icons.Filled.Clear, // ????????????
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            model.baseSendText = ""
                        }) // ?????????????????????????????????????????????text
                },
            )
        }

    }

}