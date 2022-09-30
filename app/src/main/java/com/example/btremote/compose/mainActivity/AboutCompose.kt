package com.example.btremote.compose.mainActivity

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.R
import com.example.btremote.app.App.Companion.appContext
import com.example.btremote.tools.APKVersionInfoUtils
import com.example.btremote.tools.addQQ
import com.example.btremote.tools.openMail


@Composable
fun About(context: Context = LocalContext.current) {
    val myEmail = "cy1512634242@163.com"
    val myQQ = "1512634242"
    var visiblePay by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(), shape = RoundedCornerShape(5.dp), elevation = 2.dp
        ) {
            Column() {
                Row(
                    modifier = Modifier
                        .padding(start = 30.dp, top = 30.dp)
                        .fillMaxWidth()
                        .height(50.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.Top)
                            .padding(start = 20.dp)
                    ) {
                        Text(
                            text = "Bluetooth Remote",
                            fontSize = 18.sp,
                            fontWeight = FontWeight(700)
                        )
                        Text(
                            text = "version ${APKVersionInfoUtils.getVersionCode(context)}",
                            fontSize = 11.sp
                        )
                        //Text(text = "版本 1.23.1")
                    }
                }
                Text(
                    text = "可以通过蓝牙进行基本的串口操作，也可以使用遥控界面远程控制你的设备",
                    modifier = Modifier.padding(start = 30.dp, top = 10.dp, end = 30.dp)
                )
                Text(
                    text = "Designed by SITICE",
                    modifier = Modifier.padding(start = 30.dp, top = 10.dp),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        Row(
            modifier = Modifier
                .padding(start = 40.dp, end = 40.dp, top = 20.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Icon(painter = painterResource(id = R.drawable.baseline_add_shopping_cart_black_36dp), contentDescription = null)
//                Text(text = "购买产品", fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
//            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { openMail(context, myEmail) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_bug_report_black_36dp),
                        contentDescription = null
                    )
                }
                Text(text = "报告缺陷", fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
            }
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                IconButton(onClick = {  }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.baseline_info_black_36dp),
//                        contentDescription = null
//                    )
//                }
//                Text(text = "关于", fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
//            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { visiblePay = !visiblePay }) {
                    Icon(
                        painter = painterResource(id = R.drawable.donation),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(text = "捐赠", fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        AnimatedVisibility(
            visible = visiblePay,
        ) {
            Image(
                painter = painterResource(id = R.drawable.cyalipay),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
        }

//                Row(
//                    modifier = Modifier
//                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
//                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Image(
//                            painter = painterResource(id = R.drawable.qq), contentDescription = null, modifier = Modifier
//                                .size(30.dp)
//                        )
//                        Text(text = "QQ : ", modifier = Modifier.padding(start = 5.dp))
//                    }
//                    Image(
//                        painter = painterResource(id = R.drawable.send), contentDescription = null, modifier = Modifier
//                            .size(30.dp)
//                    )
//                }
        Spacer(modifier = Modifier.height(20.dp))
        Divider(modifier = Modifier.fillMaxWidth())
        Text(
            text = "联系我们",
            fontSize = 18.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(start = 30.dp, top = 30.dp)
        )
        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_email_black_36dp),
                    contentDescription = null,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(20.dp))
                SelectionContainer {
                    Text(text = myEmail, modifier = Modifier.padding(start = 5.dp))
                }
            }
            Image(
                painter = painterResource(id = R.drawable.send),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        openMail(context, myEmail)
                    }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Divider(modifier = Modifier.fillMaxWidth())
        Text(
            text = "作者",
            fontSize = 18.sp,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(start = 30.dp, top = 10.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.touxiang),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .clip(shape = RoundedCornerShape(50))
                            .border(
                                width = 2.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(50)
                            )
                    )
                    Text(
                        text = "SITICE",
                        modifier = Modifier.padding(start = 10.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.send),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            addQQ(context, myQQ)
                        }
                )
            }

        }
    }
}

//@Composable
//@Preview
//fun AboutPreview() {
//    About()
//}