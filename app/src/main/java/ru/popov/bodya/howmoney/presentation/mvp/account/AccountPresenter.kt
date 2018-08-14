package ru.popov.bodya.howmoney.presentation.mvp.account

import com.arellomobile.mvp.InjectViewState
import ru.popov.bodya.core.extensions.connect
import ru.popov.bodya.core.mvp.AppPresenter
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet
import ru.popov.bodya.howmoney.presentation.ui.common.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 *  @author popovbodya
 */
@InjectViewState
class AccountPresenter @Inject constructor(
        private val walletInteractor: WalletInteractor,
        private val rxSchedulersTransformer: RxSchedulersTransformer,
        private val router: Router) : AppPresenter<AccountView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        navigateToWalletScreen()
    }

    fun navigateToPeriodicTransactionsScreen() {
        router.navigateTo(Screens.PERIODIC_SCREEN)
    }

    fun navigateToSettingsScreen() {
        router.newScreenChain(Screens.SETTINGS_SCREEN)
    }

    fun navigateToStatsScreen() {
        router.navigateTo(Screens.STATS_SCREEN)
    }

    fun navigateToWalletScreen() {
        router.newRootScreen(Screens.WALLET_SCREEN)
    }

    fun onBackPressed() {
        router.exit()
    }

    fun createWallet(wallet: Wallet) {
        walletInteractor.createWallet(wallet)
                .compose(rxSchedulersTransformer.ioToMainTransformerCompletable())
                .subscribe(viewState::onWalletCreated)
                .connect(compositeDisposable)
    }
}