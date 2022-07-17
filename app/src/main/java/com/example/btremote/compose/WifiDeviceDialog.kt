package com.example.btremote.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.btremote.ui.theme.Blue

@Composable
fun ShowWifiDevice(openDialog: MutableState<Boolean>) {
    var switch by remember { mutableStateOf(false) }
    if (openDialog.value) {
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .height(600.dp)
                ) {
                    Row(Modifier.padding(20.dp)) {
                        Text(text = "WIFI", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(20.dp))
                        Switch(
                            modifier = Modifier.padding(top = 2.dp),
                            checked = switch,
                            colors = SwitchDefaults.colors(checkedThumbColor = Blue),
                            onCheckedChange = {
                                switch = it
                            })
                    }

                }
            }
        }
    }
}
