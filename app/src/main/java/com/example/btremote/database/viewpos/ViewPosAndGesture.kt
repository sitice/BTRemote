package com.example.btremote.database.viewpos

import androidx.compose.ui.geometry.Offset
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ViewPosAndGesture {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
    @ColumnInfo(name = "viewName")
    var viewName: String = ""
    @ColumnInfo(name = "offsetX")
    var offsetX: Float = 0f
    @ColumnInfo(name = "offsetY")
    var offsetY: Float = 0f
    @ColumnInfo(name = "scale")
    var scale: Float = 0f
    @ColumnInfo(name = "rotationAngle")
    var rotationAngle: Float = 0f
}
