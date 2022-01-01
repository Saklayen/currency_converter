package com.saklayen.currencyconverter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saklayen.currencyconverter.BuildConfig.DATABASE_ENCRYPTION_KEY
import com.saklayen.currencyconverter.database.dao.TransactionDao
import com.saklayen.currencyconverter.database.dao.WalletsDao
import com.saklayen.currencyconverter.database.model.Transaction
import com.saklayen.currencyconverter.database.model.Wallet
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [
        Wallet::class,
    Transaction::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val walletsDao: WalletsDao
    abstract val transactionDao: TransactionDao

    companion object {
        private const val databaseName = "app_database"

        fun buildDatabase(context: Context): AppDatabase {
            val key = DATABASE_ENCRYPTION_KEY.toCharArray()
            val passphrase = SQLiteDatabase.getBytes(key)
            val supportFactory = SupportFactory(passphrase)
            return Room
                .databaseBuilder(context, AppDatabase::class.java, databaseName)
                .openHelperFactory(supportFactory)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
