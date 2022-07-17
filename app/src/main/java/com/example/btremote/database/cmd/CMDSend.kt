package com.example.btremote.database.cmd

import androidx.compose.ui.geometry.Offset
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CMDSend() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
    @ColumnInfo(name = "cmdName")
    var name: String = ""
    @ColumnInfo(name = "type")
    var type: Int = 0
    @ColumnInfo(name = "text")
    var text: String? = null
}
