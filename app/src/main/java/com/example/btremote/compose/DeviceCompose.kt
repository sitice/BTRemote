package com.example.btremote.compose

import android.graphics.drawable.PaintDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.btremote.R
import com.example.btremote.ui.theme.Blue
import com.example.btremote.ui.theme.DeepBlue
import com.example.btremote.ui.theme.roundedCorner10dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceCompose( navController: NavHostController) {
    Text(text = "已连接设备", modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 15.dp), fontSize = 20.sp)
    Row(
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp)
    ) {
        Card(
            onClick = {
                navController.navigate(Screens.productScreen)
            },
            modifier = Modifier
                .padding(end = 10.dp)
                .height(120.dp)
                .width(100.dp), backgroundColor = DeepBlue, shape = roundedCorner10dp
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "未知设备", Modifier.padding(top = 20.dp), color = Color.White)
                Icon(
                    painter = painterResource(R.drawable.unknown),
                    contentDescription = null,
                    tint = Blue,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}
