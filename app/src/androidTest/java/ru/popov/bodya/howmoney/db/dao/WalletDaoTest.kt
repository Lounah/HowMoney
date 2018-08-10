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
import ru.popov.bodya.howmoney.data.database.dao.WalletDao
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.domain.wallet.models.Type
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class WalletDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var walletDao: WalletDao

    private val walletForTesting = Wallet(1, 0.0, Type.CASH, Currency.RUB, "Test")

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).build()
        walletDao = appDatabase.walletDao
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun returnsWallet_whenItInserted() {
        walletDao.insert(walletForTesting)

        walletDao.getAllWallets()
                .subscribeOn(Schedulers.newThread())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue {
                    it.size == 1
                }
    }

    @Test
    fun returnsWallet_byId() {
        val wallet = walletForTesting
        walletDao.insert(wallet)

        walletDao.getWalletById(1)
                .subscribeOn(Schedulers.newThread())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue {
                    it == walletForTesting
                }
    }

    @Test
    fun returnsEmptyById_whenDbIsEmpty() {
        walletDao.getWalletById(1)
                .subscribeOn(Schedulers.newThread())
                .test()
                .assertEmpty()
    }

    @Test
    fun returnsEmptyAll_whenDbIsEmpty() {
        walletDao.getAllWallets()
                .subscribeOn(Schedulers.newThread())
                .test()
                .assertEmpty()
    }

    @Test
    fun updatesBalance() {
        walletDao.insert(walletForTesting)

        walletDao.updateWalletBalance(1, 100.0)

        walletDao.getWalletById(1)
                .subscribeOn(Schedulers.newThread())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue {
                    it -> it.amount == 100.0
                }
    }

    @Test
    fun increasesBalance() {
        walletDao.insert(walletForTesting.copy(amount = 200.0))

        walletDao.increaseWalletBalance(1, 100.0)

        walletDao.getWalletById(1)
                .subscribeOn(Schedulers.newThread())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue {
                    it -> it.amount == 300.0
                }
    }

    @Test
    fun deletesWallet() {
        walletDao.insert(walletForTesting)

        walletDao.deleteWalletById(1)

        walletDao.getAllWallets()
                .subscribeOn(Schedulers.newThread())
                .test()
                .assertEmpty()
    }
}