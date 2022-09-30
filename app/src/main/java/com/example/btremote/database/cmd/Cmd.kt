package com.example.btremote.database.cmd

import androidx.compose.foundation.gestures.TransformableState
import androidx.room.*

@Entity
data class Cmd(
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name:String,
    var type: Int,
    var cmd:String
)