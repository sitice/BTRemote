package com.example.btremote.protocol

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

enum class RecAddressType {
    ANDROID_ADDRESS_TYPE,
    DESKTOP_ADDRESS_TYPE,
    DF_CHARIOT_ADDRESS_TYPE,
    DF_DRONE_ADDRESS_TYPE,
    CUSTOM_ADDRESS_TYPE,
    UNIVERSAL_ADDRESS_TYPE,
    UNKNOWN_TYPE
}

private var recAddressType: RecAddressType? = null

private var data: ByteArray = ByteArray(50)
private var recAddress = 0//通道类型

private var recType = 0//控制类型
private var controlType = 0//具体类型

private var state = 0
private var recLength = 0
private var nowCount = 5
fun readUartData(s: ByteArray) {
    for (i in s) {
        when (state) {
            0 -> {
                if (i == 0xdf.toByte()) {//帧头
                    data.fill(0)
                    data[0] = i
                    state = 1
                }
            }
            1 -> {
                state = if (i in ANDROID_ADDRESS || i == UNIVERSAL_ADDRESS.toByte()) {
                    data[1] = i
                    2
                } else {
                    0
                }
            }
            2 -> {
                recAddress = i.toInt()
                recAddressType = when (recAddress) {
                    in ANDROID_ADDRESS ->
                        RecAddressType.ANDROID_ADDRESS_TYPE
                    in DESKTOP_ADDRESS -> RecAddressType.DESKTOP_ADDRESS_TYPE
                    in CUSTOM_ADDRESS -> RecAddressType.CUSTOM_ADDRESS_TYPE
                    in DF_CHARIOT_ADDRESS -> RecAddressType.DF_CHARIOT_ADDRESS_TYPE
                    in DF_DRONE_ADDRESS -> RecAddressType.DF_DRONE_ADDRESS_TYPE
                    UNIVERSAL_ADDRESS -> RecAddressType.UNIVERSAL_ADDRESS_TYPE
                    else -> RecAddressType.UNKNOWN_TYPE
                }
                data[2] = i
                state = 3
            }
            3 -> {
                recType = i.toInt()
                data[3] = i
                state = 4
            }
            4 -> {
                controlType = i.toInt()
                when (recType) {
                    APP_REMOTE_TYPE -> {
                        recLength = when (controlType) {
                            else -> {
                                state = 0
                                0
                            }
                        }
                    }
                    CONTROL_TYPE -> {
                        recLength = when (controlType) {
                            else -> {
                                state = 0
                                0
                            }
                        }
                    }
                    SETTING_TYPE -> {
                        recLength = when (controlType) {
                            else -> {
                                state = 0
                                0
                            }
                        }
                    }
                    READ_DATA_TYPE -> {
                        recLength = when (controlType) {
                            else -> {
                                state = 0
                                0
                            }
                        }
                    }
                    RETURN_DATA_TYPE -> {
                        recLength = when (controlType) {
                            else -> {
                                state = 0
                                0
                            }
                        }
                    }
                    CUSTOM_TYPE -> {
                        recLength = when (controlType) {
                            else -> {
                                state = 0
                                0
                            }
                        }
                    }
                }
                state = if (recLength > 255) {
                    0
                } else {
                    data[4] = i
                    5
                }
            }
            5 -> {
                data[nowCount++] = i
                if (nowCount - 5 >= recAddress){
                    state = 6
                }
            }
            6 -> {
                state = if (i == 0xFD.toByte()) {
                    data[nowCount] = i
                    7
                } else 0
            }
            7 -> {
                if (i == data.sum().toByte()) {
                    analyzeData()
                }
                recAddress = 0
                recType = 0//控制类型
                controlType = 0//具体类型
                state = 0
            }
            else -> {
                recAddress = 0
                recType = 0//控制类型
                controlType = 0//具体类型
                state = 0
            }
        }
    }
}

fun analyzeData() {

}