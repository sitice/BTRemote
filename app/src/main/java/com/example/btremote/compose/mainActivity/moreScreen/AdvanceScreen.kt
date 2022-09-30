package com.example.btremote.compose.mainActivity.moreScreen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.btremote.R
import com.example.btremote.compose.Screens
import com.example.btremote.tools.ToastUtil
import com.example.btremote.ui.theme.gradient5
import com.example.btremote.ui.theme.gradient6
import com.example.btremote.ui.theme.gradient8

@Composable
fun AdvanceScreen(navController: NavController,context: Context = LocalContext.current) {
    Column() {
//        Box(
//            modifier = Modifier
//                .height(200.dp)
//                .fillMaxWidth()
//                .padding(30.dp)
//                .background(brush = gradient5, shape = RoundedCornerShape(10.dp))
//                .clickable {
//                    navController.navigate(Screens.protocolScreen)
//                }
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.dianfuprotcol),
//                contentDescription = null,
//                modifier = Modifier
//                    .align(Alignment.CenterStart)
//                    .offset(x = 30.dp)
//            )
//            Text(text = "点浮通信协议",modifier = Modifier
//                .align(Alignment.CenterStart)
//                .offset(x = 120.dp), fontSize = 30.sp, fontWeight = FontWeight(800)
//            )
//        }
//
//        Box(
//            modifier = Modifier
//                .height(200.dp)
//                .fillMaxWidth()
//                .padding(30.dp)
//                .background(brush = gradient6, shape = RoundedCornerShape(10.dp))
//                .clickable {
//                    ToastUtil.toast(context = context,"暂不支持")
//                },
//            contentAlignment = Alignment.Center
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.logo_mavlink_small),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(250.dp)
//            )
//        }

        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(30.dp)
                .background(brush = gradient8, shape = RoundedCornerShape(10.dp))
                .clickable {
                    navController.navigate(Screens.esp32camScreen)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.cam),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = 30.dp)
            )
            Text(text = "ESP32 CAM",modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 120.dp), fontSize = 30.sp, fontWeight = FontWeight(800)
            )
        }
    }
}