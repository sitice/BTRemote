package com.example.btremote.database.remoteWidget

import android.content.Context
import androidx.room.*

@Database(entities = [RemoteWidget::class],version = 1)
abstract class RemoteWidgetDatabase:RoomDatabase() {
    abstract fun remoteWidgetDao(): RemoteWidgetDao
    companion object {
        private const val DB_NAME = "remoteWidget.db"

        @Volatile
        private var instance: RemoteWidgetDatabase? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    RemoteWidgetDatabase::class.java, DB_NAME
                ).build()
            }
    }
}