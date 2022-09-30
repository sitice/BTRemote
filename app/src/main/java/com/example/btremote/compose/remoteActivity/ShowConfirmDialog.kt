package com.example.btremote.compose.remoteActivity

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ShowConfirmDialog(
    text: String,
    onSureClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(onDismissRequest = onCancelClick) {
        Surface(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700)
                )

                Spacer(modifier = Modifier.height(40.dp))
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
                        onClick = onSureClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor =  Color(0x20ff1744),
                            //不可以点击的颜色
                            disabledBackgroundColor = Color.Gray
                        )
                    ) {
                        Text(
                            text = "确认",
                            color = Color(0xffff1744),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}
