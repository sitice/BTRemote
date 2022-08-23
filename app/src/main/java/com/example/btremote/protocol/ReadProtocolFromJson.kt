package com.example.btremote.protocol

import android.content.Context
import com.alibaba.fastjson.JSON
import java.io.IOException
import java.nio.charset.Charset


object ReadProtocolFromJson {
    fun readAssetsFile(context: Context, fileName: String): String? {
        try {
            val i = context.assets.open(fileName)
            val fileLength = i.available()
            val buffer = ByteArray(fileLength)
            val readLength = i.read(buffer)
            i.close()
            return String(buffer, Charset.forName("utf-8"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getLocalProtocol(json: String): List<Protocol> {
        return JSON.parseArray(
            json,
            Protocol::class.java
        )
    }
}