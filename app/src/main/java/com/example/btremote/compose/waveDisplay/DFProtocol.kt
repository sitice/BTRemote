package com.example.btremote.compose.waveDisplay

import kotlinx.coroutines.flow.MutableStateFlow


data class DFProtocol(
    val name: String,
    val mAddress: Byte,
    val targetAddress: Byte,
    val frameType: Byte,
    val ctrlType: Byte,
    val len: Byte,
    val dataList: List<Data>
) {
    class Data(
        val dataName: String,
        val dataType: String,
        var color: Int,
        var isSelect: Boolean,
        val dataFlow: MutableStateFlow<String?>
    ){
        val setData:(str:String)->Unit = {}
    }
}