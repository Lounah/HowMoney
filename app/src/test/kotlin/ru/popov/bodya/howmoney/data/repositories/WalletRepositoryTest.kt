package ru.popov.bodya.howmoney.data.repositories

import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.*
import ru.popov.bodya.howmoney.data.database.dao.WalletDao
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.domain.wallet.models.Currency

@RunWith(JUnit4::class)
class WalletRepositoryTest {

    private lateinit var walletDao: WalletDao
    private lateinit var walletRepository: WalletRepository

    private val walletForTesting = Wallet(0, 1.0, Type.CASH, Currency.RUB, "Основной кошелек")

    @Before
    fun setUp() {
        walletDao = Mockito.mock(WalletDao::class.java)
        walletRepository = WalletRepository(walletDao)
    }

    @Test
    fun wallets_getWithSuccess() {
        val expected = listOf(walletForTesting)
        `when`(walletDao.getAllWallets()).thenReturn(Flowable.just(expected))

        walletRepository.getWallets().test().assertValue(expected)

        verify(walletDao).getAllWallets()
        verifyNoMoreInteractions(walletDao)
    }

    @Test
    fun wallet_getsWithError() {
        val expectedException = RuntimeException()
        `when`(walletDao.getAllWallets()).thenReturn(Flowable.error(expectedException))

        walletRepository.getWallets().test().assertError(expectedException)

        verify(walletDao).getAllWallets()
        verifyNoMoreInteractions(walletDao)
    }

    @Test
    fun wallet_getsById() {
        val walletId = 0
        `when`(walletDao.getWalletById(walletId)).thenReturn(Flowable.just(walletForTesting))

        walletRepository.getWalletById(walletId).test().assertValue(walletForTesting)

        verify(walletDao).getWalletById(walletId)
        verifyNoMoreInteractions(walletDao)
    }

    @Test
    fun wallet_added() {
        walletRepository.addWallet(walletForTesting).test()

        verify(walletDao).insert(walletForTesting)
        verifyNoMoreInteractions(walletDao)
    }

    @Test
    fun wallet_deletes() {
        walletRepository.deleteWallet(walletForTesting.id).test()

        verify(walletDao).deleteWalletById(walletForTesting.id)
        verifyNoMoreInteractions(walletDao)
    }

    @Test
    fun walletBalance_updates() {
        val walletId = 0
        val newBalance = 1.0

        walletRepository.updateWalletBalance(walletId, newBalance).test()

        verify(walletDao).updateWalletBalance(walletId, newBalance)
        verifyNoMoreInteractions(walletDao)
    }

    @Test
    fun walletBalance_increases() {
        val walletId = 0
        val inc = 1.0

        walletRepository.increaseWalletBalance(walletId, inc).test()

        verify(walletDao).increaseWalletBalance(walletId, inc)
        verifyNoMoreInteractions(walletDao)
    }
}