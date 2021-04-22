package com.medhelp.ui.localDb.roomDb.dao

import androidx.room.*
import com.medhelp.ui.localDb.roomDb.bean.UserProfile

@Dao
public interface UserDao{
    @Query("SELECT * FROM userInfo")
    fun getAll(): List<UserProfile?>?

    @Insert
    fun insert(userProfile: UserProfile?)

    @Delete
    fun delete(userProfile: UserProfile?)

    @Update
    fun update(userProfile: UserProfile?)

    @Query("DELETE FROM userInfo")
    fun deleteAll()
}