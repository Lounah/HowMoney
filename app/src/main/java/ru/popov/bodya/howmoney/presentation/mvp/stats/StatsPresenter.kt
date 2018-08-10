package ru.popov.bodya.howmoney.presentation.mvp.stats

import com.arellomobile.mvp.InjectViewState
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import ru.popov.bodya.core.extensions.connect
import ru.popov.bodya.core.mvp.AppPresenter
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class StatsPresenter @Inject constructor(
        private val walletInteractor: WalletInteractor,
        private val rxSchedulersTransformer: RxSchedulersTransformer,
        private val router: Router
) : AppPresenter<StatsView>() {

    private var selectedWalletId: Int = 1

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        fetchAllWallets()
        fetchStats(0)
    }

    fun fetchStats(walletId: Int) {
        walletInteractor.getStatsByWalletId(walletId)
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showStatsByCategory)
                .connect(compositeDisposable)
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

    fun onWalletChanged(newWalletId: Int) {
        selectedWalletId = newWalletId
        fetchTransactionsSumByWalletId(newWalletId)
        fetchStats(newWalletId)
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
}