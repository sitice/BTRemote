package com.example.btremote.compose.product

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.btremote.R
import com.example.btremote.RemoteActivity
import com.example.btremote.compose.Screens
import com.example.btremote.ui.theme.gradient4

@Composable
fun ProductScreen(navController: NavController,context: Context = LocalContext.current) {


    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.zhanche),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .width(300.dp),
                contentScale = ContentScale.Crop
            )
        }
        Divider(modifier = Modifier.fillMaxWidth())


//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 50.dp)
//        ) {
//            Spacer(
//                modifier = Modifier
//                    .padding(top = 35.dp)
//                    .height(50.dp)
//                    .width(3.dp)
//                    .background(color = Color(255, 102, 0))
//            )
//            Column {
//                Text(text = "Product", modifier = Modifier.padding(top = 30.dp, start = 30.dp), fontSize = 20.sp, color = Color.White)
//                Text(text = "description", fontSize = 11.sp, modifier = Modifier.padding(start = 30.dp, top = 10.dp), color = Color(0xfff2f2f2))
//            }
//        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(top = 20.dp)

        ) {

            Row(Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .height(130.dp)
                        .weight(1f),
                    elevation = 0.dp,
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = Color(0xff191a23)
                ) {
                    Box(modifier = Modifier.fillMaxSize())
                    {
                        Image(
                            modifier = Modifier
                                .size(110.dp)
                                .rotate(90f)
                                .align(Alignment.Center),
                            painter = painterResource(id = R.drawable.dw),
                            contentDescription = null
                        )
                        Text(
                            text = "90°",
                            color = Color.Red,
                            fontSize = 15.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 30.dp)
                        .height(130.dp)
                        .weight(1f)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        elevation = 0.dp,
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = Color(0x8fffa33b)
                    ) {
                        Box(modifier = Modifier.fillMaxSize())
                        {
                            Image(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(35.dp)
                                    .align(Alignment.CenterStart),
                                painter = painterResource(id = R.drawable.battery),
                                contentDescription = null
                            )
                            Text(
                                text = "12.12V",
                                color = Color(0xffff6600),
                                fontSize = 21.sp,
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        elevation = 0.dp,
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = Color(0xffbcf1d0)
                    ) {
                        Box(modifier = Modifier.fillMaxSize())
                        {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(35.dp)
                                    .align(Alignment.CenterStart),
                                painter = painterResource(id = R.drawable.version),
                                contentDescription = null,
                                tint = Color(0xff039c03)
                            )
                            Text(
                                text = "V1.0",
                                color = Color(0xff039c03),
                                fontSize = 21.sp,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 20.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = 2.dp,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                        .background(gradient4, RoundedCornerShape(10.dp)).clickable {
                        (context as Activity).startActivity(
                            Intent(
                                context,
                                RemoteActivity::class.java
                            )
                        )
                    },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.game),
                        contentDescription = null,
                        tint = Color(0xffff8533)
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(text = "开始遥控", color = Color.White, fontSize = 25.sp)
                }
            }
            Button(
                onClick = {
                    navController.navigate(Screens.settingScreen)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    //不可以点击的颜色
                    disabledBackgroundColor = Color.Gray
                )
            ) {
                Image(painter = painterResource(id = R.drawable.setting), contentDescription = null)
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "setting", color = Color.DarkGray, fontSize = 18.sp)
            }
            Button(
                onClick = {
                    navController.navigate(Screens.debugScreen)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    //不可以点击的颜色
                    disabledBackgroundColor = Color.Gray
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.debugger),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "debug", color = Color.DarkGray, fontSize = 18.sp)
            }
        }

    }

}

//@Composable
//@Preview
//fun ProductPreview() {
//    Product()
//}
