package com.example.btremote.compose.mainActivity.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.R

@Composable
fun ProductSetting() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(30.dp)
            .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "设置", fontSize = 25.sp)
            Box(contentAlignment = Alignment.TopCenter) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.reset), contentDescription = null, modifier = Modifier.size(25.dp))
                }
                Text(text = "恢复", fontSize = 9.sp, modifier = Modifier.offset(y = 35.dp))
            }
        }
        Row {
            Button(
                onClick = { /*TODO*/ },
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

                Text(text = "保存", color = Color.White, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(30.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xffff0040),
                    //不可以点击的颜色
                    disabledBackgroundColor = Color.Gray
                )
            ) {
                Text(text = "取消", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
@Preview
fun SettingPreview() {
    ProductSetting()
}

