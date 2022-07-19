package com.example.btremote.protocol

/**
 * 判断该字符串是否为中文
 * @param string
 * @return
 */
fun isChinese(string: String, indexL: Int = 19968, indexH: Int = 40868): Boolean {
    for (element in string) {
        if (element.code !in indexL..indexH) {
            return false
        }
    }
    return true
}

fun isLowercase(string: String, indexL: Int = 97, indexH: Int = 122): Boolean {
    for (element in string) {
        if (element.code !in indexL..indexH) {
            return false
        }
    }
    return true
}

fun isUppercase(string: String, indexL: Int = 65, indexH: Int = 90): Boolean {
    for (element in string) {
        if (element.code !in indexL..indexH) {
            return false
        }
    }
    return true
}

fun isNumber(string: String, indexL: Int = 48, indexH: Int = 57): Boolean {
    for (element in string) {
        if (element.code !in indexL..indexH) {
            return false
        }
    }
    return true
}
fun isHex(string: String):Boolean{
    val regex = Regex("^[A-Fa-f0-9]+$")
    return string.matches(regex)
}