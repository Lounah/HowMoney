package ru.popov.bodya.howmoney.presentation.settings

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import ru.popov.bodya.core.rx.RxSchedulersStub
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.core.rx.RxSchedulersTransformerImpl
import ru.popov.bodya.howmoney.data.database.preferences.SharedPreferencesWrapper
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.presentation.mvp.settings.SettingsPresenter
import ru.popov.bodya.howmoney.presentation.mvp.settings.SettingsView
import ru.popov.bodya.howmoney.presentation.mvp.settings.`SettingsView$$State`
import ru.terrakok.cicerone.Router

@RunWith(MockitoJUnitRunner::class)
class SettingsPresenterTest {

    @Mock
    lateinit var walletInteractor: WalletInteractor

    @Mock
    lateinit var sharedPrefsWrapper: SharedPreferencesWrapper

    @Mock
    lateinit var router: Router

    @Mock
    lateinit var view: SettingsView

    @Mock
    lateinit var viewState: `SettingsView$$State`

    private lateinit var settingsPresenter: SettingsPresenter

    private lateinit var rxSchedulersTransformer: RxSchedulersTransformer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val mRxSchedulers = Mockito.spy(RxSchedulersStub())
        rxSchedulersTransformer = RxSchedulersTransformerImpl(mRxSchedulers)
        settingsPresenter = SettingsPresenter(sharedPrefsWrapper, walletInteractor, rxSchedulersTransformer, router)
        settingsPresenter.setViewState(viewState)
    }

    @Test
    fun attachView_fetchesExchangeRate() {
        `when`(sharedPrefsWrapper.getFavExchangeRate()).thenReturn("RUB")

        settingsPresenter.attachView(view)

        verify(viewState).showFavCurrency("RUB")
    }

    @Test
    fun clearData_showsResult() {
        `when`(walletInteractor.deleteAllTransactions()).thenReturn(Completable.complete())

        settingsPresenter.clearData()

        verify(viewState).showSuccessDeletionResult()
        verifyNoMoreInteractions(viewState)
    }

    @Test
    fun favCurrency_updates() {
        settingsPresenter.updateFavCurrency("RUB")
        verify(sharedPrefsWrapper).setNewFavCurrency("RUB")
    }
}

