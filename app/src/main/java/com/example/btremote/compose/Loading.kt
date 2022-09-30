package com.example.btremote.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.*
import com.example.btremote.R

@Composable
fun Loading(backgroundColor:Color){
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(backgroundColor)
                    .fillMaxWidth(),
               contentAlignment = Alignment.Center
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading5_color_data))
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    speed = 1f,
                    iterations = LottieConstants.IterateForever // 设置永不结束
                )
                LottieAnimation(
                    composition = composition, progress = progress,
                    Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                )
            }
        }
    }
}