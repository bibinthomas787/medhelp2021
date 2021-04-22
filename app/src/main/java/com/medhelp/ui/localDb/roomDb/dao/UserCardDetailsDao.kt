package com.medhelp.ui.localDb.roomDb.dao

import androidx.room.*
import com.medhelp.ui.localDb.roomDb.bean.UserCardDetails

@Dao
public interface UserCardDetailsDao{
    @Query("SELECT * FROM userCardDetails")
    fun getAll(): List<UserCardDetails?>?

    @Insert
    fun insert(filesBean: UserCardDetails?)

    @Delete
    fun delete(filesBean: UserCardDetails?)

    @Update
    fun update(filesBean: UserCardDetails?)

    @Query("DELETE FROM userCardDetails")
    fun deleteAll()
} 