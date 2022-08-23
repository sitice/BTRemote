package com.example.btremote.protocol

import java.util.*
import kotlin.experimental.and

// char转换为byte[2]数组
fun getByteArray(c: Char): ByteArray {
    val b = ByteArray(2)
    b[1] = (c.code and 0xff00 shr 8).toByte()
    b[0] = (c.code and 0x00ff).toByte()
    return b
}

// int转换为UByte[4]数组
fun getByteArray(i: Int): ByteArray {
    val b = ByteArray(4)
    b[3] = (i and -0x1000000 shr 24).toByte()
    b[2] = (i and 0x00ff0000 shr 16).toByte()
    b[1] = (i and 0x0000ff00 shr 8).toByte()
    b[0] = (i and 0x000000ff).toByte()
    return b
}

// UInt转换为UByte[4]数组
fun getByteArray(i: UInt): ByteArray {
    val b = ByteArray(4)
    b[3] = (i.toLong() and -0x1000000 shr 24).toByte()
    b[2] = (i.toLong() and 0x00ff0000 shr 16).toByte()
    b[1] = (i.toLong() and 0x0000ff00 shr 8).toByte()
    b[0] = (i.toLong() and 0x000000ff).toByte()
    return b
}

// short转换为UByte[4]数组
fun getByteArray(i: Short): ByteArray {
    val b = ByteArray(2)
    b[1] = (i.toInt() and 0x0000ff00 shr 8).toByte()
    b[0] = (i.toInt() and 0x000000ff).toByte()
    return b
}

// UShort转换为UByte[4]数组
fun getByteArray(i: UShort): ByteArray {
    val b = ByteArray(2)
    b[1] = (i.toInt() and 0x0000ff00 shr 8).toByte()
    b[0] = (i.toInt() and 0x000000ff).toByte()
    return b
}

// float转换为UByte[4]数组
fun getByteArray(f: Float): ByteArray {
    val intBits = java.lang.Float.floatToIntBits(f) //将float里面的二进制串解释为int整数
    return getByteArray(intBits)
}

// double转换为byte[8]数组
fun getByteArray(d: Double): ByteArray {
    val longBits = java.lang.Double.doubleToLongBits(d)
    return getByteArray(longBits)
}


// long转换为byte[8]数组
fun getByteArray(l: Long): ByteArray {
    val b = ByteArray(8)
    b[7] = (0xff and (l shr 56).toInt()).toByte()
    b[6] = (0xff and (l shr 48).toInt()).toByte()
    b[5] = (0xff and (l shr 40).toInt()).toByte()
    b[4] = (0xff and (l shr 32).toInt()).toByte()
    b[3] = (0xff and (l shr 24).toInt()).toByte()
    b[2] = (0xff and (l shr 16).toInt()).toByte()
    b[1] = (0xff and (l shr 8).toInt()).toByte()
    b[0] = (0xff and l.toInt()).toByte()
    return b
}

// 从byte数组的index处的连续两个字节获得一个char
fun getChar(arr: ByteArray): Char? {
    return when (arr.size) {
        0 -> null
        1 -> arr[0].toInt().toChar()
        else -> (0x00ff and arr[0].toInt() or (0xff00 and arr[1].toInt() shl 8)).toChar()
    }
}

// 从UByte数组的index处的连续两个字节获得一个UShort
fun getUShort(arr: ByteArray): UShort? {
    return when (arr.size) {
        0 -> null
        1 -> arr[0].toUShort()
        else -> (0x00ff and arr[0].toInt() or (0xff00 and arr[1].toInt() shl 8)).toUShort()
    }
}

fun getShort(arr: ByteArray): Short? {
    return when (arr.size) {
        0 -> null
        1 -> arr[0].toShort()
        else -> (0x00ff and arr[0].toInt() or (0xff00 and arr[1].toInt() shl 8)).toShort()
    }
}

fun getInt(arr: ByteArray): Int? {
    return when (arr.size) {
        0 -> null
        1 -> arr[0].toInt()
        2 -> 0x000000ff and (arr[0].toInt()) or
                (0x0000ff00 and (arr[+1].toInt() shl 8))
        3 -> 0x000000ff and (arr[0].toInt()) or
                (0x0000ff00 and (arr[1].toInt() shl 8)) or
                (0x00ff0000 and (arr[2].toInt() shl 16))
        else -> 0x000000ff and (arr[0].toInt()) or
                (0x0000ff00 and (arr[1].toInt() shl 8)) or
                (0x00ff0000 and (arr[2].toInt() shl 16)) or
                (-0x100000 and (arr[3].toInt() shl 24))

    }
}

fun getUInt(arr: ByteArray): UInt? {
    return when (arr.size) {
        0 -> null
        1 -> arr[0].toUInt()
        2 -> (0x000000ff and (arr[0].toInt()) or
                (0x0000ff00 and (arr[1].toInt() shl 8))).toUInt()
        3 -> (0x000000ff and (arr[0].toInt()) or
                (0x0000ff00 and (arr[1].toInt() shl 8)) or
                (0x00ff0000 and (arr[2].toInt() shl 16))).toUInt()
        else -> (0x000000ff and (arr[0].toInt()) or
                (0x0000ff00 and (arr[1].toInt() shl 8)) or
                (0x00ff0000 and (arr[2].toInt() shl 16)) or
                (-0x100000 and (arr[3].toInt() shl 24))).toUInt()

    }
}

// 从byte数组的index处的连续8个字节获得一个long
fun getLong(arr: ByteArray): Long? {
    return when (arr.size) {
        0 -> null
        1 -> (0x000000000000ff00L and (arr[1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[0].toLong())
        2 -> (0x0000000000ff0000L and (arr[2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[0].toLong())
        3 -> (0x00000000ff000000L and (arr[3].toLong() shl 24)) or
                (0x0000000000ff0000L and (arr[2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[0].toLong())
        4 -> (0x000000ff00000000L and (arr[4].toLong() shl 32)) or
                (0x00000000ff000000L and (arr[3].toLong() shl 24)) or
                (0x0000000000ff0000L and (arr[2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[0].toLong())
        5 -> (0x0000ff0000000000L and (arr[5].toLong() shl 40)) or
                (0x000000ff00000000L and (arr[4].toLong() shl 32)) or
                (0x00000000ff000000L and (arr[3].toLong() shl 24)) or
                (0x0000000000ff0000L and (arr[2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[0].toLong())
        6 -> (0x00ff000000000000L and (arr[6].toLong() shl 48)) or
                (0x0000ff0000000000L and (arr[5].toLong() shl 40)) or
                (0x000000ff00000000L and (arr[4].toLong() shl 32)) or
                (0x00000000ff000000L and (arr[3].toLong() shl 24)) or
                (0x0000000000ff0000L and (arr[2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[0].toLong())
        else -> -0x100000000000000L and (arr[7].toLong() shl 56) or
                (0x00ff000000000000L and (arr[6].toLong() shl 48)) or
                (0x0000ff0000000000L and (arr[5].toLong() shl 40)) or
                (0x000000ff00000000L and (arr[4].toLong() shl 32)) or
                (0x00000000ff000000L and (arr[3].toLong() shl 24)) or
                (0x0000000000ff0000L and (arr[2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[0].toLong())
    }
}

@ExperimentalUnsignedTypes
fun getFloat(arr: ByteArray): Float? {
    return getInt(arr)?.let { java.lang.Float.intBitsToFloat(it) }
}


// 从byte数组的index处的连续8个字节获得一个double
fun getDouble(arr: ByteArray): Double? {
    return getLong(arr)?.let { java.lang.Double.longBitsToDouble(it) }
}

// 从byte数组的index处的连续两个字节获得一个char
fun getChar(arr: ByteArray, index: Int): Char {
    return (0x00ff and arr[index].toInt() shl 8 or (0xff00 and arr[index + 1].toInt())).toChar()
}

// 从UByte数组的index处的连续两个字节获得一个UShort
fun getUShort(arr: ByteArray, index: Int): UShort {
    return when (arr.size - index) {
        0 -> 0u
        1 -> arr[index].toUShort()
        else -> (0x00ff and arr[index].toInt() or (0xff00 and arr[index + 1].toInt() shl 8)).toUShort()
    }
}

fun getShort(arr: ByteArray, index: Int): Short? {
    return when (arr.size - index) {
        0 -> null
        1 -> arr[index].toShort()
        else -> (0x00ff and arr[index].toInt() or (0xff00 and arr[index + 1].toInt() shl 8)).toShort()
    }
}

fun getInt(arr: ByteArray, index: Int): Int? {
    return when (arr.size - index) {
        0 -> null
        1 -> arr[index].toInt()
        2 -> 0x000000ff and (arr[index].toInt()) or
                (0x0000ff00 and (arr[index + 1].toInt() shl 8))
        3 -> 0x000000ff and (arr[index].toInt()) or
                (0x0000ff00 and (arr[index + 1].toInt() shl 8)) or
                (0x00ff0000 and (arr[index + 2].toInt() shl 16))
        else -> 0x000000ff and (arr[index].toInt()) or
                (0x0000ff00 and (arr[index + 1].toInt() shl 8)) or
                (0x00ff0000 and (arr[index + 2].toInt() shl 16)) or
                (-0x100000 and (arr[index + 3].toInt() shl 24))

    }
}

fun getUInt(arr: ByteArray, index: Int): UInt? {
    return when (arr.size - index) {
        0 -> null
        1 -> arr[index].toUInt()
        2 -> (0x000000ff and (arr[index].toInt()) or
                (0x0000ff00 and (arr[index + 1].toInt() shl 8))).toUInt()
        3 -> (0x000000ff and (arr[index].toInt()) or
                (0x0000ff00 and (arr[index + 1].toInt() shl 8)) or
                (0x00ff0000 and (arr[index + 2].toInt() shl 16))).toUInt()
        else -> (0x000000ff and (arr[index].toInt()) or
                (0x0000ff00 and (arr[index + 1].toInt() shl 8)) or
                (0x00ff0000 and (arr[index + 2].toInt() shl 16)) or
                (-0x100000 and (arr[index + 3].toInt() shl 24))).toUInt()

    }
}

// 从byte数组的index处的连续8个字节获得一个long
fun getLong(arr: ByteArray, index: Int): Long? {
    return when (arr.size - index) {
        0 -> null
        1 -> (0x000000000000ff00L and (arr[index + 1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[index].toLong())
        2 -> (0x0000000000ff0000L and (arr[index + 2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[index + 1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[index].toLong())
        3 -> (0x00000000ff000000L and (arr[index + 3].toLong() shl 24)) or
                (0x0000000000ff0000L and (arr[index + 2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[index + 1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[index].toLong())
        4 -> (0x000000ff00000000L and (arr[index + 4].toLong() shl 32)) or
                (0x00000000ff000000L and (arr[index + 3].toLong() shl 24)) or
                (0x0000000000ff0000L and (arr[index + 2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[index + 1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[index].toLong())
        5 -> (0x0000ff0000000000L and (arr[index + 5].toLong() shl 40)) or
                (0x000000ff00000000L and (arr[index + 4].toLong() shl 32)) or
                (0x00000000ff000000L and (arr[index + 3].toLong() shl 24)) or
                (0x0000000000ff0000L and (arr[index + 2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[index + 1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[index].toLong())
        6 -> (0x00ff000000000000L and (arr[index + 6].toLong() shl 48)) or
                (0x0000ff0000000000L and (arr[index + 5].toLong() shl 40)) or
                (0x000000ff00000000L and (arr[index + 4].toLong() shl 32)) or
                (0x00000000ff000000L and (arr[index + 3].toLong() shl 24)) or
                (0x0000000000ff0000L and (arr[index + 2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[index + 1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[index].toLong())
        else -> -0x100000000000000L and (arr[index + 7].toLong() shl 56) or
                (0x00ff000000000000L and (arr[index + 6].toLong() shl 48)) or
                (0x0000ff0000000000L and (arr[index + 5].toLong() shl 40)) or
                (0x000000ff00000000L and (arr[index + 4].toLong() shl 32)) or
                (0x00000000ff000000L and (arr[index + 3].toLong() shl 24)) or
                (0x0000000000ff0000L and (arr[index + 2].toLong() shl 16)) or
                (0x000000000000ff00L and (arr[index + 1].toLong() shl 8)) or
                (0x00000000000000ffL and arr[index].toLong())
    }
}

@ExperimentalUnsignedTypes
fun getFloat(arr: ByteArray, index: Int): Float? {
    return getInt(arr, index)?.let { java.lang.Float.intBitsToFloat(it) }
}


// 从byte数组的index处的连续8个字节获得一个double
fun getDouble(arr: ByteArray, index: Int): Double? {
    return getLong(arr, index)?.let { java.lang.Double.longBitsToDouble(it) }
}

/**
 * 字符串转化成为16进制字符串
 * @param s 字符串
 * @return String
 */
fun strToHex(s: String): String {
    var str = ""
    for (element in s) {
        val ch = element.code
        val s4 = Integer.toHexString(ch).uppercase(Locale.ROOT)
        str += if (s4.length < 2) {
            val hc = "0$s4"
            hc
        } else {
            s4
        }
    }
    return str
}

/**
 * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
 * @param src byte[] data
 * @return hex string
 */
fun bytesToHexString(src: ByteArray?): String? {
    val stringBuilder = StringBuilder("")
    if (src == null || src.isEmpty()) {
        return null
    }
    for (i in src.indices) {
        val v: Byte = src[i] and 0xFF.toByte()
        val hv = Integer.toHexString(v.toInt())
        if (hv.length < 2) {
            stringBuilder.append(0)
        }
        stringBuilder.append(hv)
    }
    return stringBuilder.toString()
}

fun string2Byte(s: String): ByteArray {
    val result = ByteArray(s.length / 2)
    var j = 0
    for (i in 0 until (s.length + 1) / 2) {
        result[i] = char2Byte(s[j++])
        result[i] = (char2Byte(s[j++]) + (result[i].toInt() shl 4)).toByte()
    }
    return result
}

fun char2Byte(c: Char): Byte {
    if (c in 'a'..'f') {
        return (c - 'a' + 10).toByte()
    } else if (c in 'A'..'F') {
        return (c - 'A' + 10).toByte()
    } else if (c in '0'..'9') {
        return (c - '0').toByte()
    }
    return (-1).toByte()
}
/**
 * 字符串转化成为ByteArray
 * @param s 字符串
 * @return String
 */
fun strToByteArray(s: String): ByteArray {
    val a = ByteArray(s.length)
    for (i in s.indices) {
        val ch = s[i].code
        a[i] = Integer.toHexString(ch).toByte()
    }
    return a
}

/**
 * 字节型数转化为字符串
 * @param src 字节型数组
 * @return 16进制型数的字符串
 */

fun bytesToHexStrings(src: ByteArray): String {
    val hexString = StringBuilder(src.size)
    for (b in src) {
        val v: Int = b.toInt() and 0xFF
        val hv = Integer.toHexString(v).uppercase(Locale.ROOT)
        if (hv.length < 2) {
            val hc = "0$hv"
            hexString.append(hc)
        } else {
            hexString.append(hv)
        }
    }
    return hexString.toString()
}

/**
 * 字节型数转化为字符串打印出来
 * @param src 字节型数组
 * @return 16进制型数的字符串
 */
fun logBytesToHexStrings(src: String): String {
    val stringBuilder = java.lang.StringBuilder()
    for (i in src.indices step 2) stringBuilder.append("0x" + src[i] + src[i + 1] + ",")
    stringBuilder.delete(stringBuilder.length - 1, stringBuilder.length)
    return stringBuilder.toString()
}