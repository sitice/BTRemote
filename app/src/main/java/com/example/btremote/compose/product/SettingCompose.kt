package com.example.btremote.compose.product

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
fun Setting() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(30.dp)
            .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Setting", fontSize = 25.sp)
            Box(modifier = Modifier.width(50.dp)) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.reset), contentDescription = null, modifier = Modifier.size(25.dp))
                }
                Text(text = "reset", modifier = Modifier.padding(start = 9.dp, top = 30.dp))
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

                Text(text = "Save", color = Color.White, fontSize = 18.sp)
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
                Text(text = "Cancel", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
@Preview
fun SettingPreview() {
    Setting()
}

