package ru.popov.bodya.howmoney.presentation.mvp.periodic

import com.arellomobile.mvp.InjectViewState
import ru.popov.bodya.core.extensions.connect
import ru.popov.bodya.core.mvp.AppPresenter
import ru.terrakok.cicerone.Router
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import javax.inject.Inject

@InjectViewState
class PeriodicTransactionsPresenter @Inject constructor(
        private val walletInteractor: WalletInteractor,
        private val rxSchedulersTransformer: RxSchedulersTransformer,
        private val router: Router
) : AppPresenter<PeriodicTransactionsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        fetchAllTransactions()
    }

    fun fetchAllTransactions() {
        walletInteractor.getAllPeriodicTransactions()
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showTransactions)
                .connect(compositeDisposable)
    }

    fun deleteTransaction(transaction: Transaction) {
        walletInteractor.deleteTransaction(transaction)
                .compose(rxSchedulersTransformer.ioToMainTransformerCompletable())
                .subscribe()
                .connect(compositeDisposable)
    }
}