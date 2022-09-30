package com.example.btremote.compose.waveDisplay

import com.example.btremote.wave.LineChartPoints
import kotlinx.coroutines.flow.MutableStateFlow

class DFProtocolData(
    var name: String,
    var color: MutableStateFlow<Int>,
    var visible: MutableStateFlow<Boolean>,
    var data: MutableStateFlow<String?>,
    var line: LineChartPoints,
    var dataCallBack:(String)->Unit
){
    fun getData(callBack:(String)->Unit){
        dataCallBack = callBack
    }
}