package ru.popov.bodya.howmoney.presentation.mvp.settings

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import ru.popov.bodya.core.extensions.connect
import ru.popov.bodya.core.mvp.AppPresenter
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.howmoney.data.database.preferences.SharedPreferencesWrapper
import ru.popov.bodya.howmoney.presentation.ui.common.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class SettingsPresenter @Inject constructor(
        private val sharedPrefsWrapper: SharedPreferencesWrapper,
        private val rxSchedulersTransformer: RxSchedulersTransformer,
        private val router: Router
) : AppPresenter<SettingsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Single.just(sharedPrefsWrapper.getFavExchangeRate())
                .compose(rxSchedulersTransformer.ioToMainTransformerSingle())
                .subscribe(viewState::showFavCurrency)
                .connect(compositeDisposable)
    }

    fun updateFavCurrency(currencyKey: String) {
        sharedPrefsWrapper.setNewFavCurrency(currencyKey)
    }

    fun onBackPressed() {
        router.navigateTo(Screens.WALLET_SCREEN)
    }

    fun navigateToAboutScreen() {
        router.navigateTo(Screens.ABOUT_SCREEN)
    }

}