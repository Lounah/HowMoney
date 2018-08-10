package ru.popov.bodya.howmoney.presentation.wallet

import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.core.rx.RxSchedulersTransformerImpl
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.presentation.mvp.wallet.WalletPresenter
import ru.popov.bodya.howmoney.presentation.mvp.wallet.WalletView
import ru.popov.bodya.howmoney.presentation.mvp.wallet.`WalletView$$State`
import ru.terrakok.cicerone.Router
import java.util.*
import ru.popov.bodya.core.rx.RxSchedulersStub
import ru.popov.bodya.howmoney.presentation.ui.common.Screens


@RunWith(MockitoJUnitRunner::class)
class WalletPresenterTest {

    @Mock
    lateinit var walletInteractor: WalletInteractor

    @Mock
    lateinit var router: Router

    @Mock
    lateinit var view: WalletView

    @Mock
    lateinit var viewState: `WalletView$$State`

    private lateinit var walletPresenter: WalletPresenter

    private lateinit var rxSchedulersTransformer: RxSchedulersTransformer

    private val transactionForTesting = Transaction(0, Currency.RUB, 1.0, Category.OTHER, 0, "OTHER", periodic = false, period = 0, date = Date())
    private val walletForTesting = Wallet(0, 1.0, Type.CASH, Currency.RUB, "Основной кошелек")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val mRxSchedulers = spy(RxSchedulersStub())
        rxSchedulersTransformer = RxSchedulersTransformerImpl(mRxSchedulers)
        walletPresenter = WalletPresenter(walletInteractor, rxSchedulersTransformer, router)
        walletPresenter.setViewState(viewState)
    }

    @Test
    fun attachView_showWalletsAndTransactions() {
        `when`(walletInteractor.getAllWallets()).thenReturn(Flowable.just(listOf(walletForTesting)))
        `when`(walletInteractor.getAllTransactions()).thenReturn(Flowable.just(listOf(transactionForTesting)))

        walletPresenter.attachView(view)

        verify(viewState).showWallets(listOf(walletForTesting))
        verify(viewState).showTransactions(listOf(transactionForTesting))
    }

    @Test
    fun transactionsSum_shouldBeShownByView() {
        `when`(walletInteractor.getIncomeSumFromAllWalletsByWalletId(walletForTesting.id))
                .thenReturn(Flowable.just(1.0))
        `when`(walletInteractor.getExpenseSumFromAllWalletsByWalletId(walletForTesting.id))
                .thenReturn(Flowable.just(1.0))

        walletPresenter.fetchTransactionsSumByWalletId(walletForTesting.id)

        verify(viewState).showSumOfAllIncomeTransactions(1.0)
        verify(viewState).showSumOfAllExpenseTransactions(0.0)
    }


    @Test
    fun transactionsSum_shouldReturn0_whenEmptyDb() {
        `when`(walletInteractor.getIncomeSumFromAllWalletsByWalletId(walletForTesting.id))
                .thenReturn(Flowable.empty())
        `when`(walletInteractor.getExpenseSumFromAllWalletsByWalletId(walletForTesting.id))
                .thenReturn(Flowable.empty())

        walletPresenter.fetchTransactionsSumByWalletId(walletForTesting.id)

        verify(viewState).showSumOfAllIncomeTransactions(0.0)
        verify(viewState).showSumOfAllExpenseTransactions(0.0)
    }

    @Test
    fun incomeTransactions_shouldBeShown() {
        `when`(walletInteractor.getAllIncomeTransactions()).thenReturn(Flowable.just(listOf(transactionForTesting)))

        walletPresenter.fetchAllIncomeTransactions()

        verify(viewState).showTransactions(listOf(transactionForTesting))
    }

    @Test
    fun expenseTransactions_shouldBeShown() {
        `when`(walletInteractor.getAllExpenseTransactions()).thenReturn(Flowable.just(listOf(transactionForTesting)))

        walletPresenter.fetchAllExpenseTransactions()

        verify(viewState).showTransactions(listOf(transactionForTesting))
    }

    @Test
    fun fabClicked_shouldOpenTransactionsScreen() {
        walletPresenter.onFabAddClicked()

        verify(router).newScreenChain(Screens.NEW_TRANSACTION_SCREEN, true)
    }

    @Test
    fun balanceExchanged_ShouldShowResult() {
        `when`(walletInteractor.getExchangeRate(Currency.RUB, Currency.USD)).thenReturn(Single.just(1.0))
        `when`(walletInteractor.getExchangeRate(Currency.RUB, Currency.EUR)).thenReturn(Single.just(1.0))

        walletPresenter.exchangeBalance(Currency.RUB, 1.0)

        verify(walletInteractor).getExchangeRate(Currency.RUB, Currency.USD)
        verify(viewState).showExchangedToUsdBalance(1.0)

        verify(walletInteractor).getExchangeRate(Currency.RUB, Currency.EUR)
        verify(viewState).showExchangedToEurBalance(1.0)
    }

    @Test
    fun balanceExchanged_ShouldShowError() {
        val expected = Throwable()
        `when`(walletInteractor.getExchangeRate(Currency.RUB, Currency.USD)).thenReturn(Single.error(expected))
        `when`(walletInteractor.getExchangeRate(Currency.RUB, Currency.EUR)).thenReturn(Single.error(expected))

        walletPresenter.exchangeBalance(Currency.RUB, 1.0)

        verify(walletInteractor).getExchangeRate(Currency.RUB, Currency.USD)
        verify(router, times(2)).showSystemMessage(ArgumentMatchers.anyString())

        verify(walletInteractor).getExchangeRate(Currency.RUB, Currency.EUR)
        verify(router, times(2)).showSystemMessage(ArgumentMatchers.anyString())
    }


}