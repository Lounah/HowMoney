package ru.popov.bodya.howmoney.presentation.mvp.stats

import com.arellomobile.mvp.InjectViewState
import ru.popov.bodya.core.mvp.AppPresenter
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class StatsPresenter @Inject constructor(
        private val walletInteractor: WalletInteractor,
        private val rxSchedulersTransformer: RxSchedulersTransformer,
        private val router: Router
) : AppPresenter<StatsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }

}