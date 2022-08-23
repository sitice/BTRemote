package com.example.btremote.database.cmd

import androidx.room.*

@Dao
interface CMDSendDao {
    @Query("SELECT * FROM CMDSend")
    fun getAllCMDSend() : List<CMDSend>

    @Query("SELECT * FROM CMDSend WHERE cmdName=(:name)")
    fun getCMDSend(name: String): CMDSend?

    @Query("SELECT * FROM CMDSend WHERE id=(:id)")
    fun getCMDSend(id: Int): CMDSend?

    @Insert
    fun insert(i: CMDSend?)

    /**
     * @return Int  1 成功 0失败
     */
    @Delete
    fun delete(i: CMDSend?): Int

    @Query("DELETE FROM CMDSend WHERE cmdName = :name")
    fun delete(name: String?): Int

    /**
     * @return Int 返回删除的条数
     */
    @Query("DELETE FROM CMDSend")
    fun deleteAll(): Int
    /**
     * @return Int  1 成功 0失败
     */
    @Update
    fun update(vararg info: CMDSend?): Int
}