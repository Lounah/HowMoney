package ru.popov.bodya.howmoney.data.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import ru.popov.bodya.howmoney.data.database.dao.ExchangeRateDao
import ru.popov.bodya.howmoney.data.database.dao.TransactionsDao
import ru.popov.bodya.howmoney.data.database.dao.WalletDao
import ru.popov.bodya.howmoney.data.database.executors.Executors
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import java.util.*

@TypeConverters(TimeStampConverter::class, CurrencyTypeConverter::class,
        TransactionCategoryConverter::class,
        WalletTypeConverter::class)
@Database(entities = [Transaction::class, Wallet::class, ExchangeRate::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val transactionsDao: TransactionsDao
    abstract val walletDao: WalletDao
    abstract val exchangeRateDao: ExchangeRateDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "app.db")
                        .fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Executors.ioThread {
                                    getInstance(context).walletDao.insert(PREPOPULATE_DATA)
                                }
                            }
                        })
                        .build()

        val PREPOPULATE_DATA = Wallet(0, 0.0, Type.CASH, Currency.RUB, "Базовый кошелек")
    }

}