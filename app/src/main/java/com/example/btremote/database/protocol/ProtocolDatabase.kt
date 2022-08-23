package com.example.btremote.database.protocol

import android.content.Context
import androidx.room.*

@Database(entities = [Protocol::class],version = 1)
abstract class ProtocolDatabase:RoomDatabase() {
    abstract fun protocolDao(): ProtocolDao
    companion object {
        private const val DB_NAME = "protocol.db"

        @Volatile
        private var instance: ProtocolDatabase? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    ProtocolDatabase::class.java, DB_NAME
                ).build()
            }
    }
}