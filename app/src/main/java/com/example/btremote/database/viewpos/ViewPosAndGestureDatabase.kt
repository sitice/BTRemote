package com.example.btremote.database.viewpos

import android.content.Context
import androidx.room.*

@Database(entities = [ViewPosAndGesture::class],version = 1)
abstract class ViewPosAndGestureDatabase:RoomDatabase() {
    abstract fun viewPosAndGestureDao(): ViewPosAndGestureDao
    companion object {
        private const val DB_NAME = "viewPosAndGesture.db"

        @Volatile
        private var instance: ViewPosAndGestureDatabase? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    ViewPosAndGestureDatabase::class.java, DB_NAME
                ).build()
            }
    }
}