package com.example.btremote.database.protocol

import androidx.room.*

@Entity
@TypeConverters(DataConverter::class)
data class Protocol(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "frameType")
    val frameType: Byte,
    @ColumnInfo(name = "ctrlType")
    val ctrlType: Byte,
    var length :Int,
    @ColumnInfo(name = "dataList")
    val dataList: List<Data>
) {
    @Entity
    data class Data(
        @PrimaryKey
        val dataName: String,
        val dataType: String
    )
}