package com.example.btremote.database.protocol

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProtocolDao {
    @Query("SELECT * FROM protocol")
    fun getAllProtocolFlow() : Flow<List<Protocol>>

    @Query("SELECT * FROM protocol")
    fun getAllProtocol() : List<Protocol>

    @Query("SELECT * FROM protocol WHERE name=(:name)")
    fun getProtocol(name: String): Flow<Protocol>

    @Insert
    fun insert(i: Protocol?)

    @Insert
    fun insert(list: List<Protocol>)

    /**
     * @return Int  1 成功 0失败
     */
    @Delete
    fun delete(i: Protocol?): Int

    @Query("DELETE FROM protocol WHERE name = :name")
    fun delete(name: String?): Int

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