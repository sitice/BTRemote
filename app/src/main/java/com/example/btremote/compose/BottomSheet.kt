package com.example.btremote.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.btremote.R

@Composable
fun BottomSheet()
{
    Row(modifier = Modifier.padding(top = 20.dp,start = 20.dp)) {
        Icon(painter = painterResource(id = R.drawable.baseline_mode_edit_outline_black_24dp)  , contentDescription = null)
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "编辑名字")
    }
    Row(modifier = Modifier.padding(top = 20.dp,start = 20.dp)) {
        Icon(painter = painterResource(id = R.drawable.baseline_delete_black_24dp)  , contentDescription = null)
        Spacer(modifier = Modifier.width(30.dp))
        Text(text = "删除")
    }
    Spacer(modifier = Modifier.fillMaxWidth().height(30.dp))
}