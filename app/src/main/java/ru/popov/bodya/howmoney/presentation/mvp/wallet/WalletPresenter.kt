package ru.popov.bodya.howmoney.presentation.mvp.wallet

import com.arellomobile.mvp.InjectViewState
import ru.popov.bodya.core.extensions.connect
import ru.popov.bodya.core.mvp.AppPresenter
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.presentation.ui.common.Screens
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

@InjectViewState
class WalletPresenter @Inject constructor(
        private val walletInteractor: WalletInteractor,
        private val rxSchedulersTransformer: RxSchedulersTransformer,
        private val router: Router
) : AppPresenter<WalletView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        fetchAllWallets()
        fetchAllTransactions()
        fetchFavsExchangeRates()
    }

    fun fetchTransactionsSumByWalletId(walletId: Int) {
        walletInteractor.getIncomeSumFromAllWalletsByWalletId(walletId)
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showSumOfAllIncomeTransactions, {viewState.showSumOfAllIncomeTransactions(0.0)})
                .connect(compositeDisposable)

        walletInteractor.getExpenseSumFromAllWalletsByWalletId(walletId)
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showSumOfAllExpenseTransactions, {viewState.showSumOfAllIncomeTransactions(0.0)})
                .connect(compositeDisposable)
    }

    private fun fetchAllWallets() {
        walletInteractor.getAllWallets()
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showWallets)
                .connect(compositeDisposable)
    }

    private fun fetchFavsExchangeRates() {
        walletInteractor.getExchangeRate(Currency.USD, Currency.RUB)
                .compose(rxSchedulersTransformer.ioToMainTransformerSingle())
                .subscribe(viewState::showFirstFavExchangeRate, {t -> router.showSystemMessage("Can't load exchange rates")})
                .connect(compositeDisposable)
        walletInteractor.getExchangeRate(Currency.EUR, Currency.RUB)
                .compose(rxSchedulersTransformer.ioToMainTransformerSingle())
                .subscribe(viewState::showSecondFavExchangeRate, {t -> router.showSystemMessage("Can't load exchange rates")})
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

    fun onAddNewIncomeTransactionFabClick() {
        router.newScreenChain(Screens.NEW_TRANSACTION_SCREEN, true)
    }

    fun onAddNewExpenseTransactionFabClick() {
        router.newScreenChain(Screens.NEW_TRANSACTION_SCREEN, false)
    }

    fun onCurrencyChanged(newCurrency: Currency, currentWalletId: Int) {

    }
}