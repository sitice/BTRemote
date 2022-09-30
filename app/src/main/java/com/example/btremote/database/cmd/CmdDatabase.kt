package com.example.btremote.database.cmd

import android.content.Context
import androidx.room.*

@Database(entities = [Cmd::class],version = 1)
abstract class CmdDatabase:RoomDatabase() {
    abstract fun cmdDao(): CmdDao
    companion object {
        private const val DB_NAME = "cmd.db"

        @Volatile
        private var instance: CmdDatabase? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    CmdDatabase::class.java, DB_NAME
                ).build()
            }
    }
}