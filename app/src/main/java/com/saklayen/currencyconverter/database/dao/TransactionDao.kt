package com.saklayen.currencyconverter.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.saklayen.currencyconverter.database.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao: BaseDao<Transaction> {
    @Query("select COUNT(rowid) from `transaction`")
    fun getTransactionsCount(): Flow<Int>?

    @Query("select SUM(commission) from `transaction`")
    fun getTotalCommissions(): Flow<Int>?
}