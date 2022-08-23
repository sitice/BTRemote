package com.example.btremote.compose.waveDisplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.btremote.app.App
import com.example.btremote.wave.LineChart
import com.example.btremote.wave.LineChartPoints

@Composable
fun WaveDisplayCompose() {

    Column {
        AndroidView(modifier = Modifier.fillMaxSize(),factory = {
            LineChart(it)
        })
    }
}