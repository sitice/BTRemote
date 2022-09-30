package com.example.btremote.compose.waveDisplay

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.R

@Composable
fun ToolBar(modifier: Modifier, open: MutableState<Boolean>) {
    Row(
        modifier = modifier

    ) {
        IconButton(
            onClick = {

            }, modifier = Modifier
                .padding(start = 10.dp)

        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_settings_black_24dp),
                    contentDescription = null
                )
                Text(text = "设置", fontSize = 11.sp)
            }
        }
        IconButton(
            onClick = {
                open.value = true
            }, modifier = Modifier
                .padding(start = 10.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_microwave_black_24dp),
                    contentDescription = null
                )
                Text(text = "波形选择", fontSize = 11.sp)
            }
        }
        IconButton(
            onClick = {

            }, modifier = Modifier
                .padding(start = 10.dp)


        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.reset),
                    contentDescription = null
                )
                Text(text = "恢复", fontSize = 11.sp)
            }
        }
        IconButton(
            onClick = {

            }, modifier = Modifier
                .padding(start = 10.dp)


        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_black_24dp),
                    contentDescription = null
                )
                Text(text = "清空", fontSize = 11.sp)
            }
        }
        IconButton(
            onClick = {

            }, modifier = Modifier
                .padding(start = 10.dp)

        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_pause_circle_black_24dp),
                    contentDescription = null
                )
                Text(text = "暂停", fontSize = 11.sp)
            }
        }
    }
}
