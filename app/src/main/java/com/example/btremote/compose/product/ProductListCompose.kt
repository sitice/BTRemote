package com.example.btremote.compose.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.R
import com.example.btremote.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductListCompose() {
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
        Text(modifier = Modifier.align(Alignment.CenterStart), text = "设备", fontSize = 20.sp)

        Surface(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 60.dp)
                .width(180.dp)
                .height(30.dp), shape = RoundedCornerShape(10.dp), color = Color(0xAfff8c8c)
        ) {
            Row (modifier = Modifier.padding(start = 10.dp), verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(id = R.drawable.baseline_error_black_24dp), contentDescription = null)
                Text(text = "No device connected", modifier = Modifier.padding(start = 10.dp),fontFamily = FontFamily.Monospace, fontSize = 11.sp)
            }
        }

        IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = { /*TODO*/ }) {
            Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_black_24dp), contentDescription = null)
        }
    }

    Row(
        modifier = Modifier
            .padding(start = 30.dp, top = 10.dp, bottom = 10.dp)
            .horizontalScroll(rememberScrollState())
    ) {

        Card(
            Modifier
                .width(120.dp)
                .height(170.dp), shape = RoundedCornerShape(10.dp), elevation = 1.dp
        ) {
            Image(painter = painterResource(id = R.drawable.zhanche), contentDescription = null)
        }
        Spacer(modifier = Modifier.width(20.dp))
        Card(
            Modifier
                .width(120.dp)
                .height(170.dp), shape = RoundedCornerShape(10.dp), elevation = 1.dp
        ) {

        }
        Spacer(modifier = Modifier.width(20.dp))
        Card(
            Modifier
                .width(120.dp)
                .height(170.dp), shape = RoundedCornerShape(10.dp), elevation = 1.dp
        ) {

        }
    }
}