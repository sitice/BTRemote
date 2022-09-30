package com.example.btremote.compose.mainActivity.mainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.*
import com.example.btremote.R
import com.example.btremote.app.App
import com.example.btremote.connect.BluetoothStatus
import com.example.btremote.ui.theme.Blue


@SuppressLint("MissingPermission")
@Composable
fun NoMoreListenMusic(
    text:String,
    bandList: List<String>,
    onClick:(band:String)->Unit
) {

    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                bandList.forEach {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {onClick(it)},
                        modifier = Modifier
                            .height(40.dp)
                            .width(120.dp),
                        elevation = null,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White
                        ),
                        border = BorderStroke(width = 2.dp, color = Color(0xff7e80f8))
                    ) {
                        Text(text = it, fontSize = 11.sp)
                    }
                }
            }
        }
    }

}
