package com.example.btremote.database.remoteWidget

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteWidgetDao {
    @Query("SELECT * FROM remoteWidget")
    fun getAllWidgetFlow() : Flow<List<RemoteWidget>>

    @Query("SELECT * FROM remoteWidget")
    fun getAllWidget() : List<RemoteWidget>

    @Query("SELECT * FROM remoteWidget WHERE name=(:name)")
    fun getWidget(name: String): Flow<RemoteWidget>

    @Insert
    fun insert(i: RemoteWidget?)

    @Insert
    fun insert(list: List<RemoteWidget>)

    /**
     * @return Int  1 成功 0失败
     */
    @Delete
    fun delete(i: RemoteWidget): Int

    @Query("DELETE FROM remoteWidget WHERE name = :name")
    fun delete(name: String): Int

    /**
     * @return Int 返回删除的条数
     */
    @Query("DELETE FROM remoteWidget")
    fun deleteAll(): Int
    /**
     * @return Int  1 成功 0失败
     */
    @Update
    fun update(vararg info: RemoteWidget?): Int
}