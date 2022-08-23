package com.example.btremote.protocol


/**
 * 匀速平动
 * @param x
 * @param y
 * @param speed
 * @param delay
 * @return 16进制型数的字符串
 */
fun uniformSpeedCtrl(x: Short, y: Short, speed: UByte, delay: UShort): ByteArray {
    val a = ByteArray(14)
    val xArray = getByteArray(x)
    val yArray = getByteArray(y)
    val delayArray = getByteArray(delay)
    var i = 0
    a[i++] = 0xdf.toByte()
    a[i++] = 0x01//发送目标
    a[i++] = 0xE0.toByte()//本机地址
    a[i++] = 0x02//control
    a[i++] = 0x01//control type
    a[i++] = xArray[0]
    a[i++] = xArray[1]
    a[i++] = yArray[0]
    a[i++] = yArray[1]
    a[i++] = speed.toByte()
    a[i++] = delayArray[0]
    a[i++] = delayArray[1]
    a[i++] = 0xfd.toByte()
    for (index in 0 until i) {
        a[i] = (a[index] + a[i]).toByte()
    }
    return a
}

/**
 * 自适应调速平动控制
 * @param x
 * @param y
 * @param speed
 * @param delay
 * @return 16进制型数的字符串
 */
fun variableSpeedCtrl(x: Short, y: Short, speed: UByte, delay: UShort): ByteArray {
    val a = ByteArray(14)
    val xArray = getByteArray(x)
    val yArray = getByteArray(y)
    val delayArray = getByteArray(delay)
    var i = 0
    a[i++] = 0xdf.toByte()
    a[i++] = 0x01//发送目标
    a[i++] = 0xE0.toByte()//本机地址
    a[i++] = 0x02//control
    a[i++] = 0x02//control type
    a[i++] = xArray[0]
    a[i++] = xArray[1]
    a[i++] = yArray[0]
    a[i++] = yArray[1]
    a[i++] = speed.toByte()
    a[i++] = delayArray[0]
    a[i++] = delayArray[1]
    a[i++] = 0xfd.toByte()
    for (index in 0 until i) {
        a[i] = (a[index] + a[i]).toByte()
    }
    return a
}

/**
 * 水平旋转
 * @param angle
 * @param speed
 * @param delay
 * @return 字节型数组
 */

fun spinCtrl(angle: Short, speed: UByte, delay: UShort): ByteArray {
    val a = ByteArray(11)
    val angleArray = getByteArray(angle)
    val delayArray = getByteArray(delay)
    var i = 0
    a[i++] = 0xdf.toByte()
    a[i++] = 0x01//发送目标
    a[i++] = 0xE0.toByte()//本机地址
    a[i++] = 0x02//control
    a[i++] = 0x03//control type
    a[i++] = angleArray[0]
    a[i++] = angleArray[1]
    a[i++] = speed.toByte()
    a[i++] = delayArray[0]
    a[i++] = delayArray[1]
    a[i++] = 0xfd.toByte()
    for (index in 0 until i) {
        a[i] = (a[index] + a[i]).toByte()
    }
    return a
}


/**
 * 自适应调速平动控制
 * @param x
 * @param y
 * @param speed
 * @return 16进制型数的字符串
 */
fun uniformSpeedCtrl(x: Short, y: Short, speed: UByte): ByteArray {
    val a = ByteArray(14)
    val xArray = getByteArray(x)
    val yArray = getByteArray(y)
    var i = 0
    a[i++] = 0xdf.toByte()
    a[i++] = 0x01//发送目标
    a[i++] = 0xE0.toByte()//本机地址
    a[i++] = 0x02//control
    a[i++] = 0x04//control type
    a[i++] = xArray[0]
    a[i++] = xArray[1]
    a[i++] = yArray[0]
    a[i++] = yArray[1]
    a[i++] = speed.toByte()
    a[i++] = 0xfd.toByte()
    for (index in 0 until i) {
        a[i] = (a[index] + a[i]).toByte()
    }
    return a
}


/**
 * 水平旋转
 * @param angle
 * @param speed
 * @return 字节型数组
 */

fun spinCtrl(angle: Short, speed: UByte): ByteArray {
    val a = ByteArray(11)
    val angleArray = getByteArray(angle)
    var i = 0
    a[i++] = 0xdf.toByte()
    a[i++] = 0x01//发送目标
    a[i++] = 0xE0.toByte()//本机地址
    a[i++] = 0x02//control
    a[i++] = 0x05//control type
    a[i++] = angleArray[0]
    a[i++] = angleArray[1]
    a[i++] = speed.toByte()
    a[i++] = 0xfd.toByte()
    for (index in 0 until i) {
        a[i] = (a[index] + a[i]).toByte()
    }
    return a
}



