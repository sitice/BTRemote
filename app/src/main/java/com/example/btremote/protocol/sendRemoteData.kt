package com.example.btremote.protocol

import android.util.Log
import com.example.btremote.app.App
import kotlin.math.cos
import kotlin.math.sin

val buttonsList = listOf(
    "LeftRocker",
    "RightRocker",
    "XBoxA",
    "XBoxB",
    "XBoxX",
    "XBoxY",
    "Left",
    "Right",
    "Up",
    "Down",
    "Blue",
    "Yellow",
    "Slider1"
)

var keyVal = 0
var leftDis = 0
var rightDis = 0
var leftAngle = 0f
var rightAngle = 0f
var sliderVal = 0
var pitch = 0
var roll = 0
var yaw = 0

val keyValMap = mapOf(
    "XBoxA" to 1,
    "XBoxB" to 2,
    "XBoxX" to 3,
    "XBoxY" to 4,
    "Left" to 5,
    "Right" to 6,
    "Up" to 7,
    "Down" to 8,
    "Blue" to 9,
    "Yellow" to 10,
)

fun sendRemoteVal() {
    val lx = (leftDis * sin(Math.toRadians(leftAngle.toDouble()))).toInt().toByte()
    val ly = (leftDis * cos(Math.toRadians(leftAngle.toDouble()))).toInt().toByte()
    val rx = (rightDis * sin(Math.toRadians(rightAngle.toDouble()))).toInt().toByte()
    val ry = (rightDis * cos(Math.toRadians(rightAngle.toDouble()))).toInt().toByte()
    val data = ByteArray(16)
    var cnt = 0
    data[cnt++] = 0xDF.toByte()

    data[cnt++] = 0xDF.toByte()
    data[cnt++] = 0xDF.toByte()
    data[cnt++] = 0xDF.toByte()
    data[cnt++] = 0xDF.toByte()

    data[cnt++] = lx
    data[cnt++] = ly
    data[cnt++] = rx
    data[cnt++] = ry

    data[cnt++] = keyVal.toByte()
    data[cnt++] = sliderVal.toByte()

    data[cnt++] = pitch.toByte()
    data[cnt++] = roll.toByte()
    data[cnt++] = yaw.toByte()

    data[cnt++] = 0xFD.toByte()

    data[cnt] = data.sum().toByte()
    if (keyVal != 0) keyVal = 0
    App.bluetoothService.writeData(data)
}