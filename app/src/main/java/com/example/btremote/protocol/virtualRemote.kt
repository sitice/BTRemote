import com.example.btremote.protocol.getByteArray

/**
 * 匀速平动
 * @param rockerPos
 * @param value
 * @return 16进制型数的字符串
 */
fun sendRockerVal(rockerPos: Byte, value: UByte): ByteArray {
    val a = ByteArray(14)
    var i = 0
    a[i++] = 0xdf.toByte()
    a[i++] = 0x01//发送目标
    a[i++] = 0xE0.toByte()//本机地址
    a[i++] = 0x01//control
    a[i++] = if (rockerPos > 0) 2 else 1 //control type
    a[i++] = value.toByte()
    a[i++] = 0xfd.toByte()
    for (index in 0 until i) {
        a[i] = (a[index] + a[i]).toByte()
    }
    return a
}

/**
 * 匀速平动
 * @param key
 * @return 16进制型数的字符串
 */
fun sendKeyVal( key: UByte): ByteArray {
    val a = ByteArray(14)
    var i = 0
    a[i++] = 0xdf.toByte()
    a[i++] = 0x01//发送目标
    a[i++] = 0xE0.toByte()//本机地址
    a[i++] = 0x01//control
    a[i++] = 3 //control type
    a[i++] = key.toByte()
    a[i++] = 0xfd.toByte()
    for (index in 0 until i) {
        a[i] = (a[index] + a[i]).toByte()
    }
    return a
}