package com.example.btremote.protocol

import android.util.Log

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

fun sendRemoteVal()
{

}