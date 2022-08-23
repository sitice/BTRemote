package com.example.btremote.compose.product

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.btremote.R
import com.example.btremote.RemoteActivity
import com.example.btremote.compose.Screens
import com.example.btremote.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductListCompose(navController: NavController) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, top = 10.dp, end = 30.dp),
    ) {
        Text(modifier = Modifier.align(Alignment.CenterStart), text = "设备", fontSize = 20.sp, fontWeight = FontWeight(700))

        Surface(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 60.dp)
                .height(30.dp), shape = RoundedCornerShape(10.dp), color = Color(0xAfff8c8c)
        ) {
            Row (modifier = Modifier.padding(start = 10.dp), verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(id = R.drawable.baseline_error_black_24dp), contentDescription = null)
                Text(text = "无设备连接", modifier = Modifier.padding(start = 10.dp, end = 10.dp),fontFamily = FontFamily.Monospace, fontSize = 11.sp)
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterEnd), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "更多")
            IconButton(onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_black_24dp), contentDescription = null)
            }
        }

    }

    Row(
        modifier = Modifier
            .padding(start = 30.dp, top = 10.dp, bottom = 10.dp)
            .horizontalScroll(rememberScrollState())
    ) {

        Box(
            Modifier
                .width(120.dp)
                .height(170.dp)
                .background(gradient7, RoundedCornerShape(10.dp))
                .clickable {
                    navController.navigate(Screens.productScreen)
                }
        ) {
            Image(painter = painterResource(id = R.drawable.zhanche), contentDescription = null)
        }
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            Modifier
                .width(120.dp)
                .height(170.dp)
                .background(gradient7, RoundedCornerShape(10.dp))
                .clickable {

                }
        ) {
            Image(painter = painterResource(id = R.drawable.zhanche), contentDescription = null)
        }
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            Modifier
                .width(120.dp)
                .height(170.dp)
                .background(gradient7, RoundedCornerShape(10.dp))
                .clickable {

                }
        ) {
            Image(painter = painterResource(id = R.drawable.zhanche), contentDescription = null)
        }
        Spacer(modifier = Modifier.width(30.dp))
    }
}