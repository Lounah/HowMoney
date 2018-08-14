package ru.popov.bodya.howmoney.presentation.mvp.wallet

import com.arellomobile.mvp.InjectViewState
import io.reactivex.plugins.RxJavaPlugins
import ru.popov.bodya.core.extensions.connect
import ru.popov.bodya.core.mvp.AppPresenter
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.howmoney.data.database.preferences.SharedPreferencesWrapper
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet
import ru.popov.bodya.howmoney.presentation.ui.common.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class WalletPresenter @Inject constructor(
        private val walletInteractor: WalletInteractor,
        private val rxSchedulersTransformer: RxSchedulersTransformer,
        private val router: Router
) : AppPresenter<WalletView>() {

    private var selectedWalletId: Int = 1

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        fetchAllWallets()
        fetchAllTransactions()
    }

    fun fetchTransactionsSumByWalletId(walletId: Int) {
        walletInteractor.getIncomeSumFromAllWalletsByWalletId(walletId)
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .doOnSubscribe { viewState.showSumOfAllIncomeTransactions(0.0) }
                .subscribe(viewState::showSumOfAllIncomeTransactions, { viewState.showSumOfAllIncomeTransactions(0.0) })
                .connect(compositeDisposable)

        walletInteractor.getExpenseSumFromAllWalletsByWalletId(walletId)
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .doOnSubscribe { viewState.showSumOfAllExpenseTransactions(0.0) }
                .subscribe(viewState::showSumOfAllExpenseTransactions, { viewState.showSumOfAllIncomeTransactions(0.0) })
                .connect(compositeDisposable)
    }

    fun fetchAllWallets() {
        walletInteractor.getAllWallets()
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showWallets)
                .connect(compositeDisposable)

    }

    fun fetchAllTransactions() {
        walletInteractor.getAllTransactions()
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showTransactions)
                .connect(compositeDisposable)
    }

    fun fetchAllIncomeTransactions() {
        walletInteractor.getAllIncomeTransactions()
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showTransactions)
                .connect(compositeDisposable)
    }

    fun fetchAllExpenseTransactions() {
        walletInteractor.getAllExpenseTransactions()
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showTransactions)
                .connect(compositeDisposable)
    }

    fun onFabAddClicked() {
        router.newScreenChain(Screens.NEW_TRANSACTION_SCREEN, true)
    }

    fun onWalletChanged(newWalletId: Int) {
        selectedWalletId = newWalletId
        fetchTransactionsSumByWalletId(newWalletId)
    }

    fun exchangeBalance(initialMajorCurrency: Currency, initialBalance: Double) {
        walletInteractor.getExchangeRate(initialMajorCurrency, Currency.USD)
                .compose(rxSchedulersTransformer.ioToMainTransformerSingle())
                .subscribe({ rate -> viewState.showExchangedToUsdBalance(rate * initialBalance) }, { t -> router.showSystemMessage("Can't load exchange rates") })
                .connect(compositeDisposable)

        walletInteractor.getExchangeRate(initialMajorCurrency, Currency.EUR)
                .compose(rxSchedulersTransformer.ioToMainTransformerSingle())
                .subscribe({ rate -> viewState.showExchangedToEurBalance(rate * initialBalance) }, { t -> router.showSystemMessage("Can't load exchange rates") })
                .connect(compositeDisposable)
    }

    fun deleteTransaction(transaction: Transaction) {
        walletInteractor.deleteTransaction(transaction)
                .compose(rxSchedulersTransformer.ioToMainTransformerCompletable())
                .subscribe()
                .connect(compositeDisposable)
    }
}