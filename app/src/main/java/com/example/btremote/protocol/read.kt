package com.example.btremote.protocol

import android.util.Log
import com.example.btremote.app.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.nio.ByteBuffer


val DF_CHARIOT_ADDRESS = 0x00..0x0f
val DF_DRONE_ADDRESS = 0x10..0x1f
val ANDROID_ADDRESS = 0xD0..0xDF
val DESKTOP_ADDRESS = 0xE0..0xEF
val CUSTOM_ADDRESS = 0xF0..0xFE
const val UNIVERSAL_ADDRESS = 0xFF

const val APP_REMOTE_TYPE = 0x01
const val CONTROL_TYPE = 0x02
const val SETTING_TYPE = 0x03
const val READ_DATA_TYPE = 0x04
const val RETURN_DATA_TYPE = 0x05
const val CUSTOM_TYPE = 0xFF

const val REC_MAX = 50

enum class RecAddressType {
    ANDROID_ADDRESS_TYPE,
    DESKTOP_ADDRESS_TYPE,
    DF_CHARIOT_ADDRESS_TYPE,
    DF_DRONE_ADDRESS_TYPE,
    CUSTOM_ADDRESS_TYPE,
    UNIVERSAL_ADDRESS_TYPE,
    UNKNOWN_TYPE
}

var analyzeData: (tarAddress: Byte, recAddress: Byte, frameType: Byte, ctrlType: Byte, len: Byte, data: ByteArray, recType: Int) -> Unit =
    { i: Byte, i1: Byte, i2: Byte, i3: Byte, i4: Byte, bytes: ByteArray, i5: Int -> }


fun readProtocolCallBack(mAnalyzeData: (tarAddress: Byte, recAddress: Byte, frameType: Byte, ctrlType: Byte, len: Byte, data: ByteArray, recType: Int) -> Unit) {
    analyzeData = mAnalyzeData
}

private var recAddressType =
    arrayListOf(
        RecAddressType.UNKNOWN_TYPE,
        RecAddressType.UNKNOWN_TYPE,
        RecAddressType.UNKNOWN_TYPE
    )
private var data = listOf(ByteArray(REC_MAX), ByteArray(REC_MAX), ByteArray(REC_MAX))
private var recAddress = intArrayOf(0, 0, 0)//通道类型

private var recType = intArrayOf(0, 0, 0)//控制类型
private var controlType = intArrayOf(0, 0, 0)//具体类型
private var length = intArrayOf(0, 0, 0)
private var state = intArrayOf(0, 0, 0)
private var recLength = intArrayOf(0, 0, 0)
private var nowCount = intArrayOf(6, 6, 6)
fun readUartData(s: ByteArray, index: Int) {
    for (i in s) {
        when (state[index]) {
            0 -> {
                if (i == 0xdf.toByte()) {//帧头
                    data[index].fill(0)
                    data[index][0] = i
                    state[index] = 1
                }
            }
            1 -> {
                state[index] = if (i in ANDROID_ADDRESS || i == UNIVERSAL_ADDRESS.toByte()) {
                    data[index][1] = i
                    2
                } else {
                    0
                }
            }
            2 -> {
                recAddress[index] = i.toInt()
                data[index][2] = i
                state[index] = 3
            }
            3 -> {
                recType[index] = i.toInt()
                data[index][3] = i
                state[index] = 4
            }
            4 -> {
                controlType[index] = i.toInt()
                data[index][4] = i
                state[index] = 5
            }
            5 -> {
                recLength[index] = i.toInt()
                data[index][5] = i
                state[index] = 6
            }
            6 -> {
                if (nowCount[index] - 6 >= recLength[index]) {
                    state[index] = if (i == 0xFD.toByte()) {
                        data[index][nowCount[index]] = i
                        7
                    } else {
                        nowCount[index] = 6
                        0
                    }
                } else {
                    data[index][nowCount[index]++] = i
                }
            }
            7 -> {
                if (i == data[index].sum().toByte()) {
                    analyzeData(
                        data[index][1],
                        data[index][2],
                        data[index][3],
                        data[index][4],
                        data[index][5],
                        data[index].copyOfRange(6, nowCount[index]),
                        index
                    )
                }
                recAddress[index] = 0
                recType[index] = 0//控制类型
                controlType[index] = 0//具体类型b n
                state[index] = 0
                nowCount[index] = 6
            }
            else -> {
                recAddress[index] = 0
                recType[index] = 0//控制类型
                controlType[index] = 0//具体类型
                state[index] = 0
                nowCount[index] = 6
            }
        }
    }
    //协议解析回调
    readProtocolCallBack { tarAddress: Byte,
                           recAddress: Byte,
                           frameType: Byte,
                           ctrlType: Byte,
                           len: Byte,
                           data: ByteArray,
                           recType: Int ->
        App.DFProtocolList.forEach { dfProtocol ->
            if (recAddress == dfProtocol.mAddress &&
                tarAddress == dfProtocol.targetAddress &&
                frameType == dfProtocol.frameType &&
                ctrlType == dfProtocol.ctrlType &&
                len == dfProtocol.len
            ) {
                var nowIndex = 0
                dfProtocol.dataList.forEach { dFProtocolData ->
                    dFProtocolData.dataFlow.value = when (dFProtocolData.dataType) {
                        "u8" -> {
                            nowIndex++
                            data[nowIndex - 1].toUByte().toString()
                        }
                        "u16" -> {
                            nowIndex += 2
                            getUShort(data, nowIndex - 2).toString()
                        }
                        "u32" -> {
                            nowIndex += 4
                            getUInt(data, nowIndex - 4).toString()
                        }
                        "s8" -> {
                            nowIndex++
                            data[nowIndex - 1].toString()
                        }
                        "s16" -> {
                            nowIndex += 2
                            getShort(data, nowIndex - 2).toString()
                        }
                        "s32" -> {
                            nowIndex += 4
                            getInt(data, nowIndex - 4).toString()
                        }
                        "sfloat" -> {
                            nowIndex += 4
                            (getInt(data, nowIndex - 4)!! / 100f).toString()
                        }
                        "ufloat" -> {
                            nowIndex += 4
                            (getUInt(data, nowIndex - 4)!!.toLong() / 100f).toString()
                        }
                        else -> {
                            null
                        }
                    }
                }
            }
        }
    }
}

