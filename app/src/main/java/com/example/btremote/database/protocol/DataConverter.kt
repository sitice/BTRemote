package com.example.btremote.database.protocol

import androidx.room.TypeConverter
import com.alibaba.fastjson.JSON

class DataConverter {
    @TypeConverter
    fun objectToString(list: List<Protocol.Data>): String {
        return JSON.toJSONString(list)
    }
    @TypeConverter
    fun stringToJson(json: String): List<Protocol.Data> {
        return JSON.parseArray(json, Protocol.Data::class.java)
    }
}