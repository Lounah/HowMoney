package ru.popov.bodya.howmoney.data.repositories


import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import ru.popov.bodya.howmoney.data.network.api.CurrenciesRateApiWrapper
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import java.util.*

@RunWith(JUnit4::class)
class WalletInteractorTest {

    private lateinit var transactionsRepository: TransactionsRepository
    private lateinit var walletRepository: WalletRepository
    private lateinit var currencyRateRepository: CurrencyRateRepository
    private lateinit var currenciesRateApiWrapper: CurrenciesRateApiWrapper
    private lateinit var walletInteractor: WalletInteractor


    private lateinit var transactionForTesting: Transaction
    private lateinit var walletForTesting: Wallet

    @Before
    fun setUp() {
        transactionForTesting = Transaction(0, Currency.RUB, 1.0, Category.OTHER, 0, "OTHER", periodic = false, period = 0, date = Date())
        walletForTesting = Wallet(0, 1.0, Type.CASH, Currency.RUB, "Основной кошелек")
        walletRepository = mock(WalletRepository::class.java)
        currenciesRateApiWrapper = mock(CurrenciesRateApiWrapper::class.java)
        currencyRateRepository = mock(CurrencyRateRepository::class.java)
        transactionsRepository = mock(TransactionsRepository::class.java)
        walletInteractor = WalletInteractor(currencyRateRepository, walletRepository, transactionsRepository)
    }

    @Test
    fun wallet_create() {
        walletInteractor.createWallet(walletForTesting).test().assertResult()
        verify(walletRepository).addWallet(walletForTesting)
        verifyNoMoreInteractions(walletRepository)
    }

    @Test
    fun walletBalance_get() {
        `when`(walletRepository.getWalletById(walletForTesting.id)).thenReturn(Flowable.just(walletForTesting))

        walletInteractor.getWalletBalance(walletForTesting.id).test().assertValue(1.0)

        verify(walletRepository).getWalletById(walletForTesting.id)
        verifyNoMoreInteractions(walletRepository)
    }

    @Test
    fun walletCurrency_get() {
        `when`(walletRepository.getWalletById(walletForTesting.id)).thenReturn(Flowable.just(walletForTesting))

        walletInteractor.getMajorCurrencyForWallet(walletForTesting.id).test().assertValue(Currency.RUB)

        verify(walletRepository).getWalletById(walletForTesting.id)
        verifyNoMoreInteractions(walletRepository)
    }

    @Test
    fun wallets_get() {
        val expected = listOf(walletForTesting)
        `when`(walletRepository.getWallets()).thenReturn(Flowable.just(expected))

        walletInteractor.getAllWallets().test().assertValue(expected)

        verify(walletRepository).getWallets()
        verifyNoMoreInteractions(walletRepository)
    }

    @Test
    fun incomeTransactions_getAll() {
        val expected = listOf(transactionForTesting)
        `when`(transactionsRepository.getAllIncomeTransactions())
                .thenReturn(Flowable.just(expected))

        walletInteractor.getAllIncomeTransactions().test().assertValue(expected)

        verify(transactionsRepository).getAllIncomeTransactions()
        verifyNoMoreInteractions(transactionsRepository)
    }

    @Test
    fun expenseTransactions_getAll() {
        val expected = listOf(transactionForTesting)
        `when`(transactionsRepository.getAllExpenseTransactions())
                .thenReturn(Flowable.just(expected))

        walletInteractor.getAllExpenseTransactions().test().assertValue(expected)

        verify(transactionsRepository).getAllExpenseTransactions()
        verifyNoMoreInteractions(transactionsRepository)
    }

    @Test
    fun incomeTransactions_getByWalletId() {
        val expected = listOf(transactionForTesting)
        `when`(transactionsRepository.getAllIncomeTransactionsByWallet(walletForTesting.id))
                .thenReturn(Flowable.just(expected))

        walletInteractor.getAllIncomeTransactionsByWallet(walletForTesting.id).test().assertValue(expected)

        verify(transactionsRepository).getAllIncomeTransactionsByWallet(walletForTesting.id)
        verifyNoMoreInteractions(transactionsRepository)
    }

    @Test
    fun expenseTransactions_getByWalletId() {
        val expected = listOf(transactionForTesting)
        `when`(transactionsRepository.getAllExpenseTransactionsByWallet(walletForTesting.id))
                .thenReturn(Flowable.just(expected))

        walletInteractor.getAllExpenseTransactionsByWallet(walletForTesting.id)
                .test().assertValue(expected)

        verify(transactionsRepository).getAllExpenseTransactionsByWallet(walletForTesting.id)
        verifyNoMoreInteractions(transactionsRepository)
    }

    @Test
    fun transactions_getByWalletId() {
        val expected = listOf(transactionForTesting)
        `when`(transactionsRepository.getAllTransactionsByWallet(walletForTesting.id))
                .thenReturn(Flowable.just(expected))

        walletInteractor.getAllTransactionsByWallet(walletForTesting.id).test().assertValue(expected)

        verify(transactionsRepository).getAllTransactionsByWallet(walletForTesting.id)
        verifyNoMoreInteractions(transactionsRepository)
    }

}