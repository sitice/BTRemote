package com.example.btremote.compose.waveDisplay

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alibaba.fastjson.JSON
import com.example.btremote.app.App
import com.example.btremote.database.protocol.Protocol
import com.example.btremote.tools.readAssetsFile
import com.example.btremote.viewmodel.WaveViewModel
import com.example.btremote.wave.LineChart
import com.example.btremote.wave.LineChartPoints
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

@Composable
fun WaveDisplayCompose(
    model: WaveViewModel = viewModel(),
) {
    val protocolList = model.protocols.collectAsState(initial = listOf())
    val isOpenDialog = remember {
        mutableStateOf(false)
    }
    ProtocolListSelect(protocolList, isOpenDialog)
    Box(modifier = Modifier.fillMaxSize())
    {
        Row {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                LineChart(it)
            })
        }
        ToolBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp), isOpenDialog
        )
    }
}