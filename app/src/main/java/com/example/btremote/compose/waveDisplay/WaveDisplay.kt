package com.example.btremote.compose.waveDisplay

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.viewmodel.WaveViewModel
import com.example.btremote.wave.LineChart

@Composable
fun WaveDisplayCompose(
    model: WaveViewModel = viewModel(),
) {

    val isOpenDialog = remember {
        mutableStateOf(false)
    }
    ProtocolListSelect(isOpenDialog)
    Box(modifier = Modifier.fillMaxSize())
    {
        Row {
            WaveDataShow()
            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                LineChart(it).apply {
                    setViewModel(model)
                }
            })
        }
        ToolBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp), isOpenDialog
        )
    }
}