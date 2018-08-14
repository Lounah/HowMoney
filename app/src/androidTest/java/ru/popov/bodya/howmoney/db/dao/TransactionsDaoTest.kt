package ru.popov.bodya.howmoney.db.dao

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.popov.bodya.howmoney.data.database.AppDatabase
import ru.popov.bodya.howmoney.data.database.dao.TransactionsDao
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class TransactionsDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var transactionsDao: TransactionsDao

    private val transactionForTesting
            = Transaction(1, Currency.RUB, 1.0, Category.OTHER, 0, "OTHER", date = Date())

    private val pendingTransactionForTesting
            = Transaction(2, Currency.RUB, 1.0, Category.OTHER, 0, "OTHER", periodic = true, period = 1000, date = Date())

    private val transactionsListForTesting = listOf(transactionForTesting, pendingTransactionForTesting)

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).build()
        transactionsDao = appDatabase.transactionsDao
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun returnsTransaction_whenInserted() {
        transactionsDao.insert(transactionForTesting)

        transactionsDao.getAllTransactions()
                .subscribeOn(Schedulers.newThread())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue {
                    it.size == 1
                }
    }

    @Test
    fun returnsTransaction_byId() {
        val transaction = transactionForTesting
        transactionsDao.insert(transaction)

        transactionsDao.getTransactionById(1)
                .subscribeOn(Schedulers.newThread())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue {
                    it == transactionForTesting
                }
    }


    @Test
    fun returnsEmptyById_whenDbIsEmpty() {
        transactionsDao.getTransactionById(1)
                .subscribeOn(Schedulers.newThread())
                .test()
                .assertEmpty()
    }

    @Test
    fun returnsEmptyAll_whenDbIsEmpty() {
        transactionsDao.getAllTransactions()
                .subscribeOn(Schedulers.newThread())
                .test()
                .assertEmpty()
    }

    @Test
    fun deletesTransaction() {
        transactionsDao.insert(transactionForTesting)

        transactionsDao.deleteTransactionById(1)

        transactionsDao.getAllTransactions()
                .subscribeOn(Schedulers.newThread())
                .test()
                .assertEmpty()
    }

    @Test
    fun getsOnlyPeriodicTransactions() {
        transactionsDao.insertAll(transactionsListForTesting)

        transactionsDao.getAllPeriodicTransactions()
                .subscribeOn(Schedulers.newThread())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue {
                    it.size == 1
                }
    }
}