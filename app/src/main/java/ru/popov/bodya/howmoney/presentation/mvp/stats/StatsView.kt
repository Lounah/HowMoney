package ru.popov.bodya.howmoney.presentation.mvp.stats

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import ru.popov.bodya.core.mvp.AppView
import ru.popov.bodya.howmoney.domain.wallet.models.Stats
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet

interface StatsView : AppView {
    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showWallets(wallets: List<Wallet>)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showStatsByCategory(stats: List<Stats>)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showSumOfAllIncomeTransactions(sum: Double)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showSumOfAllExpenseTransactions(sum: Double)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showExchangedToUsdBalance(balance: Double)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showExchangedToEurBalance(balance: Double)
}