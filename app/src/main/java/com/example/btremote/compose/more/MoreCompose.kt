package com.example.btremote.compose.more

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.btremote.R
import com.example.btremote.RemoteActivity
import com.example.btremote.WaveDisplayActivity
import com.example.btremote.ui.theme.gradient1
import com.example.btremote.ui.theme.gradient2

@Composable
fun MoreShowCompose(context: Context = LocalContext.current) {
    Divider(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp, bottom = 20.dp))
    Row {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.smooth_chart))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            speed = 1f, // 加快播放速度（2倍速）
            iterations = LottieConstants.IterateForever // 设置永不结束
        )
        val composition1 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.game_symbols))
        val progress1 by animateLottieCompositionAsState(
            composition = composition,
            speed = 2f, // 加快播放速度（2倍速）
            iterations = LottieConstants.IterateForever // 设置永不结束
        )
        //Text(text = "波形显示", modifier = Modifier.padding(30.dp), fontSize = 20.sp)
        Card(
            modifier = Modifier
                .padding(start = 30.dp, end = 10.dp)
                .height(100.dp)
                .weight(1f)
                .clickable {
                    (context as Activity).startActivity(
                        Intent(
                            context,
                            WaveDisplayActivity::class.java
                        )
                    )
                }, shape = RoundedCornerShape(10.dp), elevation = 0.dp
        ) {
            Box(modifier = Modifier.background(gradient1)) {
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 10.dp),
                    color = Color.Gray
                ) {
                    Text(
                        text = "波形显示",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier.padding(5.dp)
                    )
                }
                LottieAnimation(
                    composition = composition, progress = progress,
                    Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                )
            }
        }
        //Text(text = "遥控", modifier = Modifier.padding(30.dp), fontSize = 20.sp)
        Card(
            modifier = Modifier
                .padding(start = 10.dp, end = 30.dp)
                .height(100.dp)
                .weight(1f)
                .clickable {
                    (context as Activity).startActivity(Intent(context, RemoteActivity::class.java))
                }, shape = RoundedCornerShape(10.dp), elevation = 0.dp
        ) {
            Box(modifier = Modifier.background(gradient2)) {
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 10.dp),
                    color = Color.Gray
                ) {
                    Text(
                        text = "遥控",
                        fontSize = 11.sp,
                        color = Color.White,
                        modifier = Modifier.padding(5.dp)
                    )
                }
                LottieAnimation(
                    composition = composition1, progress = progress1,
                    Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                        .align(Alignment.Center)
                )
            }

        }

    }
}