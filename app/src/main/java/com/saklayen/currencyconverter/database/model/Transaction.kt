package com.saklayen.currencyconverter.database.model

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
@Fts4
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val rowid: Int = 0,
    val fromWalletId: String,
    val toWalletId: String,
    val fromAmount: String,
    val toAmount: String,
    val commission: String
)
