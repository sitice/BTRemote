package com.example.btremote.database.cmd

import android.content.Context
import androidx.room.*

@Database(entities = [CMDSend::class],version = 1)
abstract class CMDSendDatabase:RoomDatabase() {
    abstract fun CMDSendDao(): CMDSendDao
    companion object {
        private const val DB_NAME = "CMDSend.db"

        @Volatile
        private var instance: CMDSendDatabase? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    CMDSendDatabase::class.java, DB_NAME
                ).build()
            }
    }
}