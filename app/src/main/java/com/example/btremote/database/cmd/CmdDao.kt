package com.example.btremote.database.cmd

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CmdDao {
    @Query("SELECT * FROM cmd")
    fun getAllCmdFlow() : Flow<List<Cmd>>

    @Query("SELECT * FROM cmd")
    fun getAllCmd() : List<Cmd>

    @Query("SELECT * FROM cmd WHERE name=(:name)")
    fun getCmd(name: String): Flow<Cmd>

    @Insert
    fun insert(i: Cmd?)

    @Insert
    fun insert(list: List<Cmd>)

    /**
     * @return Int  1 成功 0失败
     */
    @Delete
    fun delete(i: Cmd?): Int

    @Query("DELETE FROM cmd WHERE name = :name")
    fun delete(name: String?): Int

    @Query("DELETE FROM cmd WHERE name = :id")
    fun delete(id: Int): Int

    /**
     * @return Int 返回删除的条数
     */
    @Query("DELETE FROM cmd")
    fun deleteAll(): Int
    /**
     * @return Int  1 成功 0失败
     */
    @Update
    fun update(vararg info: Cmd?): Int
}