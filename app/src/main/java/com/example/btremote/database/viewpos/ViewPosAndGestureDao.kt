package com.example.btremote.database.viewpos

import androidx.room.*
import com.example.btremote.database.viewpos.ViewPosAndGesture

@Dao
interface ViewPosAndGestureDao {
    @Query("SELECT * FROM viewPosAndGesture")
    fun getViewPosAndGestures() : List<ViewPosAndGesture>

    @Query("SELECT * FROM viewPosAndGesture WHERE viewName=(:viewName)")
    fun getViewPosAndGesture(viewName: String): ViewPosAndGesture?

    @Insert
    fun insert(i: ViewPosAndGesture?)

    /**
     * @return Int  1 成功 0失败
     */
    @Delete
    fun delete(i: ViewPosAndGesture?): Int

    @Query("DELETE FROM viewPosAndGesture WHERE viewName = :viewName")
    fun delete(viewName: String?): Int

    /**
     * @return Int 返回删除的条数
     */
    @Query("DELETE FROM viewPosAndGesture")
    fun deleteAll(): Int
    /**
     * @return Int  1 成功 0失败
     */
    @Update
    fun update(vararg info: ViewPosAndGesture?): Int
}