package com.medhelp.ui.localDb.roomDb.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "userCardDetails")
data class UserCardDetails(
        @PrimaryKey(autoGenerate = true) val uid: Int ,
    @ColumnInfo(name = "cardNumber") val cardNumber: String?,
    @ColumnInfo(name = "expiryDate") val expiryDate: String?,
    @ColumnInfo(name = "cvv") val cvv: String?,
    @ColumnInfo(name = "transactionDate") val transactionDate: Long
)