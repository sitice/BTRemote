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
    @ColumnInfo(name = "mAdd")
    val mAddress: Byte,
    @ColumnInfo(name = "tarAdd")
    val targetAddress: Byte,
    @ColumnInfo(name = "frameType")
    val frameType: Byte,
    @ColumnInfo(name = "ctrlType")
    val ctrlType: Byte,
    var length :Int,
    var isOpen:Boolean,
    @ColumnInfo(name = "dataList")
    val dataList: List<Data>
) {
    @Entity
    data class Data(
        @PrimaryKey
        val dataName: String,
        val dataType: String,
        var color:Int,
        var select:Boolean = false
    )
}