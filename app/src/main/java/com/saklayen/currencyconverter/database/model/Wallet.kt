package com.saklayen.currencyconverter.database.model

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "wallet")
@Fts4
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    val rowid: Int = 0,
    val currencyName: String,
    val balance: Float
)
