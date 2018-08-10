package ru.popov.bodya.howmoney.presentation.mvp.account

import com.arellomobile.mvp.InjectViewState
import ru.popov.bodya.core.mvp.AppPresenter
import ru.popov.bodya.howmoney.presentation.ui.common.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 *  @author popovbodya
 */
@InjectViewState
class AccountPresenter @Inject constructor(private val router: Router) : AppPresenter<AccountView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        navigateToWalletScreen()
    }

    fun navigateToSettingsScreen() {
        router.newScreenChain(Screens.SETTINGS_SCREEN)
    }

    fun navigateToWalletScreen() {
        router.newRootScreen(Screens.WALLET_SCREEN)
    }

    fun onBackPressed() {
        router.exit()
    }
}