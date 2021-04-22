package com.medhelp.ui.localDb.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medhelp.ui.localDb.roomDb.bean.UserCardDetails
import com.medhelp.ui.localDb.roomDb.bean.UserProfile
import com.medhelp.ui.localDb.roomDb.dao.UserCardDetailsDao
import com.medhelp.ui.localDb.roomDb.dao.UserDao

@Database(entities = [UserCardDetails::class, UserProfile::class], exportSchema = true, version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userCardDetailsDap(): UserCardDetailsDao?
    abstract fun userDao(): UserDao?
}
