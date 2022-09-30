package com.example.btremote.compose.waveDisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.viewmodel.WaveViewModel

@Composable
fun WaveDataShow(model: WaveViewModel = viewModel()) {
    val list by model.dfProtocolDataFlow.collectAsState()
    Column(modifier = Modifier.width(100.dp).fillMaxHeight()) {
        list.forEach { pro ->
            val color by pro.color.collectAsState()
            val visible by pro.visible.collectAsState()
            val data by pro.data.collectAsState()
            Row(modifier = Modifier.background(if (visible) Color.White else Color.Gray).clickable {
                model.setWaveVisible(pro,visible)
            }) {
                Spacer(modifier = Modifier
                    .width(20.dp)
                    .height(50.dp)
                    .background(Color(color)))
                Text(text = data?:"")
            }
        }
    }
}