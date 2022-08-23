package com.example.btremote.protocol

data class Protocol(
    val name: String,
    val mAddress: Byte,
    val targetAddress: Byte,
    val frameType: Byte,
    val ctrlType: Byte,
    val dataList: ArrayList<Data>
){
    data class Data(val dataName: String,val dataType: String)
}