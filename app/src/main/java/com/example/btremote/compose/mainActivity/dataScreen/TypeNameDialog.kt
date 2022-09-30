package com.example.btremote.compose.mainActivity.dataScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.btremote.ui.theme.roundedCorner10dp

@Composable
fun TypeNameDialog(
    text: String,
    isError:Boolean,
    onCancelClick: () -> Unit,
    onSureClick: () -> Unit,
    onTextChange: (str: String) -> Unit
) {

    Dialog(
        onDismissRequest = onCancelClick
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(20.dp)
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "请输入指令名字", fontWeight = FontWeight(700), fontSize = 18.sp)
                Spacer(modifier = Modifier.height(30.dp))
                TextField(
                    textStyle = TextStyle(fontSize = 11.sp),
                    // 指定下划线颜色
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent, // 有焦点时的颜色，透明
                        unfocusedIndicatorColor = Color.Transparent, // 无焦点时的颜色，绿色
                        errorIndicatorColor = Color.Red, // 错误时的颜色，红色
                        disabledIndicatorColor = Color.Gray // 不可用时的颜色，灰色
                    ),
                    isError = isError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = roundedCorner10dp,
                    value = text, // 显示文本
                    onValueChange = onTextChange, // 文字改变时，就赋值给text
                    label = { Text(text = "名字") }, // label是Input
                    leadingIcon = @Composable {
                        Image(imageVector = Icons.Filled.Clear, // 清除图标
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                onTextChange("")
                            }) // 给图标添加点击事件，点击就清空text

                    },
                    placeholder = @Composable {
                        Text(
                            text = "请输入名字",
                            fontSize = 11.sp
                        )
                    }) // 不输入内容时的占位符
                Spacer(modifier = Modifier.height(30.dp))
                Row {
                    Button(
                        onClick = onCancelClick,
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
                        onClick = onSureClick,
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

                        Text(text = "确定", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

