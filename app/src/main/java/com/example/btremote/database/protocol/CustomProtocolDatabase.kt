package com.example.btremote.database.protocol

import android.content.Context
import androidx.room.*

@Database(entities = [Protocol::class],version = 1)
abstract class CustomProtocolDatabase:RoomDatabase() {
    abstract fun customProtocolDao(): ProtocolDao
    companion object {
        private const val DB_NAME = "customProtocol.db"

        @Volatile
        private var instance: CustomProtocolDatabase? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    CustomProtocolDatabase::class.java, DB_NAME
                ).build()
            }
    }
}