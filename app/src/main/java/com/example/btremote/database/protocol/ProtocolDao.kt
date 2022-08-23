package com.example.btremote.database.protocol

import androidx.room.*

@Dao
interface ProtocolDao {
    @Query("SELECT * FROM protocol")
    fun getViewPosAndGestures() : List<Protocol>

    @Query("SELECT * FROM protocol WHERE viewName=(:viewName)")
    fun getViewPosAndGesture(viewName: String): Protocol?

    @Insert
    fun insert(i: Protocol?)

    /**
     * @return Int  1 成功 0失败
     */
    @Delete
    fun delete(i: Protocol?): Int

    @Query("DELETE FROM protocol WHERE viewName = :viewName")
    fun delete(viewName: String?): Int

    /**
     * @return Int 返回删除的条数
     */
    @Query("DELETE FROM protocol")
    fun deleteAll(): Int
    /**
     * @return Int  1 成功 0失败
     */
    @Update
    fun update(vararg info: Protocol?): Int
}