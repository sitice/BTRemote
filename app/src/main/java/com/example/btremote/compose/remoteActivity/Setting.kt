package com.example.btremote.compose.remoteActivity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun Setting(){
    Column() {
        Text(text = "设置")
        Row() {
            Text(text = "不显示回传数据")
            Switch(checked = true, onCheckedChange = {})
        }
    }
}