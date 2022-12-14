package com.example.btremote.compose.mainActivity.moreScreen.dfprotocol

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.R
import com.example.btremote.viewmodel.MainViewModel

@Composable
fun DFProtocol(context: Context = LocalContext.current) {

    val model: MainViewModel = viewModel()
    val originalProtocolList by model.protocols.collectAsState(listOf())
    var isOpenOriginal by remember {
        mutableStateOf(true)
    }
    var isOpenCustom by remember {
        mutableStateOf(true)
    }

    val animateOriginal = animateFloatAsState(
        targetValue = if (isOpenOriginal) 180f else 90f,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = 0,
            easing = LinearEasing
        )
    )

    val animateCustom = animateFloatAsState(
        targetValue = if (isOpenCustom) 180f else 90f,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = 0,
            easing = LinearEasing
        )
    )

    Column(modifier = Modifier.padding(30.dp)) {
        Text(text = "????????????", fontSize = 30.sp, fontWeight = FontWeight(500))
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "????????????", fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(20.dp))
            Divider(modifier = Modifier.width(210.dp))
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = { isOpenOriginal = !isOpenOriginal }) {
                Icon(
                    painter = painterResource(id = R.drawable.sanjiao),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(animateOriginal.value)
                        .size(20.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = isOpenOriginal,
        ) {
            Column {
                originalProtocolList.forEach {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "???????????????", fontSize = 10.sp)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(30.dp)
                                    .background(Color.Blue, RoundedCornerShape(15.dp))
                            ) {
                                Text(
                                    text = String.format("0X%x", it.frameType),
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "????????????", fontSize = 10.sp)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(30.dp)
                                    .background(Color.Blue, RoundedCornerShape(15.dp))
                            ) {
                                Text(
                                    text = String.format("0X%x", it.ctrlType),
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        it.dataList.forEach {
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = it.dataName + ":" + it.dataType, fontSize = 10.sp)
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(30.dp)
                                        .background(Color.Blue, RoundedCornerShape(15.dp))
                                ) {
                                    Text(text = "", color = Color.White, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "???????????????", fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(20.dp))
            Divider(modifier = Modifier.width(200.dp))
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = { isOpenCustom = !isOpenCustom }) {
                Icon(
                    painter = painterResource(id = R.drawable.sanjiao),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(animateCustom.value)
                        .size(20.dp)
                )
            }
        }
        AnimatedVisibility(
            visible = isOpenCustom,
        ) {
            Column(Modifier.padding(top = 20.dp)) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xff1a75ff),
                        //????????????????????????
                        disabledBackgroundColor = Color.Gray
                    )
                ) {
                    Text(text = "??????", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }


}