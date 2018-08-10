package ru.popov.bodya.howmoney.presentation.mvp.addtransaction

import com.arellomobile.mvp.InjectViewState
import ru.popov.bodya.core.extensions.connect
import ru.popov.bodya.core.mvp.AppPresenter
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class AddTransactionPresenter @Inject constructor(
        private val walletInteractor: WalletInteractor,
        private val rxSchedulersTransformer: RxSchedulersTransformer,
        private val router: Router
) : AppPresenter<AddTransactionView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        walletInteractor.getAllWallets()
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showWallets)
                .connect(compositeDisposable)
    }

    fun createTransaction(transaction: Transaction) {
        walletInteractor.createTransaction(transaction)
                .compose(rxSchedulersTransformer.ioToMainTransformerCompletable())
                .subscribe(viewState::onTransactionCreated)
                .connect(compositeDisposable)
    }

    fun closeFragment(exitMsg: String) {
        router.exitWithMessage(exitMsg)
    }

}