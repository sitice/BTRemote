package com.example.btremote.database.remoteWidget

import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.room.*

@Entity
data class RemoteWidget(
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,
    var type: Int,
    var offsetX: Float,
    var offsetY: Float,
    var angle: Float,
    var scale: Float,
    var color: Int?,
    var arg1: Byte,
    var arg2: Byte
) {
    @Ignore
    var offset = mutableStateOf(Offset(x = offsetX, y = offsetY))
    @Ignore
    var zoom = mutableStateOf(scale)
    @Ignore
    var rotate = mutableStateOf(angle)
}
