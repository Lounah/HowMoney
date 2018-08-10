package ru.popov.bodya.howmoney.presentation.stats

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
import ru.popov.bodya.core.rx.RxSchedulersStub
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.core.rx.RxSchedulersTransformerImpl
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.presentation.mvp.stats.StatsPresenter
import ru.popov.bodya.howmoney.presentation.mvp.stats.StatsView
import ru.popov.bodya.howmoney.presentation.mvp.stats.`StatsView$$State`
import ru.terrakok.cicerone.Router

@RunWith(MockitoJUnitRunner::class)
class StatsPresenterTest {

    @Mock
    lateinit var walletInteractor: WalletInteractor

    @Mock
    lateinit var router: Router

    @Mock
    lateinit var view: StatsView

    @Mock
    lateinit var viewState: `StatsView$$State`

    private lateinit var statsPresenter: StatsPresenter

    private lateinit var rxSchedulersTransformer: RxSchedulersTransformer

    private val transactionForTesting = Transaction(0, Currency.RUB, 1.0, Category.OTHER, 0, "OTHER", periodic = false, period = 0, date = java.util.Date())
    private val walletForTesting = Wallet(0, 1.0, Type.CASH, Currency.RUB, "Основной кошелек")
    private val statsForTesting = Stats(Category.OTHER, 1.0, 0.0)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val mRxSchedulers = spy(RxSchedulersStub())
        rxSchedulersTransformer = RxSchedulersTransformerImpl(mRxSchedulers)
        statsPresenter = StatsPresenter(walletInteractor, rxSchedulersTransformer, router)
        statsPresenter.setViewState(viewState)
    }

    @Test
    fun attachView_showWalletsAndTransactions() {
        `when`(walletInteractor.getAllWallets()).thenReturn(Flowable.just(listOf(walletForTesting)))
        `when`(walletInteractor.getStatsByWalletId(0)).thenReturn(Flowable.just(listOf(statsForTesting)))

        statsPresenter.attachView(view)

        verify(viewState).showWallets(listOf(walletForTesting))
        verify(viewState).showStatsByCategory(listOf(statsForTesting))
    }

    @Test
    fun fetchStats_shouldShowOnView() {
        `when`(walletInteractor.getStatsByWalletId(0)).thenReturn(Flowable.just(listOf(statsForTesting)))

        statsPresenter.fetchStats(0)

        verify(viewState).showStatsByCategory(listOf(statsForTesting))
    }

    @Test
    fun fetchStats_shouldShowEmpty() {
        `when`(walletInteractor.getStatsByWalletId(0)).thenReturn(Flowable.just(emptyList()))

        statsPresenter.fetchStats(0)

        verify(viewState).showStatsByCategory(emptyList())
    }

    @Test
    fun balanceExchanged_ShouldShowResult() {
        `when`(walletInteractor.getExchangeRate(Currency.RUB, Currency.USD)).thenReturn(Single.just(1.0))
        `when`(walletInteractor.getExchangeRate(Currency.RUB, Currency.EUR)).thenReturn(Single.just(1.0))

        statsPresenter.exchangeBalance(Currency.RUB, 1.0)

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

        statsPresenter.exchangeBalance(Currency.RUB, 1.0)

        verify(walletInteractor).getExchangeRate(Currency.RUB, Currency.USD)
        verify(router, times(2)).showSystemMessage(ArgumentMatchers.anyString())

        verify(walletInteractor).getExchangeRate(Currency.RUB, Currency.EUR)
        verify(router, times(2)).showSystemMessage(ArgumentMatchers.anyString())
    }

    @Test
    fun fetchWallets_shouldDisplayOnView() {
        `when`(walletInteractor.getAllWallets()).thenReturn(Flowable.just(listOf(walletForTesting)))

        statsPresenter.fetchAllWallets()

        verify(viewState).showWallets(listOf(walletForTesting))
    }
}
