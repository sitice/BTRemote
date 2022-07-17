package com.example.btremote.compose.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.R


@Composable
fun About() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(200.dp), shape = RoundedCornerShape(5.dp), elevation = 2.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
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
                        Text(text = "DF Remote", fontSize = 18.sp, fontWeight = FontWeight(500))
                        // Text(text = "版本 ${APKVersionInfoUtils.getVersionCode(appContext)}")
                        Text(text = "版本 1.23.1")
                    }
                }
                Text(text = "通过串口的远程操控，调试的应用…………………………     …", modifier = Modifier.padding(start = 30.dp, top = 10.dp))
                Text(text = "DF", modifier = Modifier.padding(start = 30.dp, top = 10.dp))
            }
        }

        Row(
            modifier = Modifier
                .padding(start = 40.dp, end = 40.dp, top = 20.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(painter = painterResource(id = R.drawable.baseline_add_shopping_cart_black_36dp), contentDescription = null)
                Text(text = "购买产品", fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(painter = painterResource(id = R.drawable.baseline_bug_report_black_36dp), contentDescription = null)
                Text(text = "报告缺陷", fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(painter = painterResource(id = R.drawable.baseline_info_black_36dp), contentDescription = null)
                Text(text = "关于点浮", fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
            }

        }

        Text(text = "联系我们", fontSize = 18.sp, fontWeight = FontWeight(500), modifier = Modifier.padding(start = 30.dp, top = 30.dp))

        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(100.dp), shape = RoundedCornerShape(5.dp), elevation = 2.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.qq), contentDescription = null, modifier = Modifier
                                .size(30.dp)
                        )
                        Text(text = "QQ : ", modifier = Modifier.padding(start = 5.dp))
                    }
                    Image(
                        painter = painterResource(id = R.drawable.send), contentDescription = null, modifier = Modifier
                            .size(30.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_phone_black_36dp), contentDescription = null, modifier = Modifier
                                .size(30.dp)
                        )
                        Text(text = "TEL : ", modifier = Modifier.padding(start = 5.dp))
                    }
                    Image(
                        painter = painterResource(id = R.drawable.send), contentDescription = null, modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }

        Text(text = "作者", fontSize = 18.sp, fontWeight = FontWeight(500), modifier = Modifier.padding(start = 30.dp, top = 10.dp))

        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(60.dp), shape = RoundedCornerShape(5.dp), elevation = 2.dp
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
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
                        Text(text = "SITICE", modifier = Modifier.padding(start = 10.dp), fontSize = 18.sp, fontWeight = FontWeight(500))
                    }
                    Image(
                        painter = painterResource(id = R.drawable.send), contentDescription = null, modifier = Modifier
                            .size(30.dp)
                    )
                }

            }
        }
    }
}

@Composable
@Preview
fun AboutPreview() {
    About()
}