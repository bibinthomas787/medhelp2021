package com.medhelp.ui.localDb.roomDb.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userInfo")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val uid: Int=0,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "profile_image") val profileImage: String?
)