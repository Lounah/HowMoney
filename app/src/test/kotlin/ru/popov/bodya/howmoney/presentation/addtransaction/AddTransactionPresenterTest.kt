package ru.popov.bodya.howmoney.presentation.addtransaction

import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import ru.popov.bodya.core.rx.RxSchedulersStub
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.core.rx.RxSchedulersTransformerImpl
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.presentation.mvp.addtransaction.AddTransactionPresenter
import ru.popov.bodya.howmoney.presentation.mvp.addtransaction.AddTransactionView
import ru.popov.bodya.howmoney.presentation.mvp.addtransaction.`AddTransactionView$$State`
import ru.terrakok.cicerone.Router

@RunWith(MockitoJUnitRunner::class)
class AddTransactionPresenterTest {

    @Mock
    lateinit var walletInteractor: WalletInteractor

    @Mock
    lateinit var router: Router

    @Mock
    lateinit var view: AddTransactionView

    @Mock
    lateinit var viewState: `AddTransactionView$$State`

    private lateinit var addTransactionPresenter: AddTransactionPresenter

    private lateinit var rxSchedulersTransformer: RxSchedulersTransformer

    private val transactionForTesting = Transaction(0, Currency.RUB, 1.0, Category.OTHER, 0, "OTHER", periodic = false, period = 0, date = java.util.Date())
    private val walletForTesting = Wallet(0, 1.0, Type.CASH, Currency.RUB, "Основной кошелек")
    private val statsForTesting = Stats(Category.OTHER, 1.0, 0.0)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val mRxSchedulers = Mockito.spy(RxSchedulersStub())
        rxSchedulersTransformer = RxSchedulersTransformerImpl(mRxSchedulers)
        addTransactionPresenter = AddTransactionPresenter(walletInteractor, rxSchedulersTransformer, router)
        addTransactionPresenter.setViewState(viewState)
    }

    @Test
    fun attachView_showWalletsAndTransactions() {
        `when`(walletInteractor.getAllWallets()).thenReturn(Flowable.just(listOf(walletForTesting)))

        addTransactionPresenter.attachView(view)

        verify(viewState).showWallets(listOf(walletForTesting))
    }

    @Test
    fun createsTemplate() {
        `when`(walletInteractor.addTransaction(transactionForTesting)).thenReturn(Completable.complete())
        `when`(walletInteractor.createTransaction(transactionForTesting)).thenReturn(Completable.complete())

        addTransactionPresenter.createTemplate(transactionForTesting)

        verify(walletInteractor).addTransaction(transactionForTesting)
        verify(walletInteractor).createTransaction(transactionForTesting.copy(id = 0, template = false))
        verify(viewState).onTransactionCreated()
    }

    @Test
    fun createsTransaction() {
        `when`(walletInteractor.createTransaction(transactionForTesting)).thenReturn(Completable.complete())

        addTransactionPresenter.createTransaction(transactionForTesting)

        verify(walletInteractor).createTransaction(transactionForTesting)
        verify(viewState).onTransactionCreated()
    }


}