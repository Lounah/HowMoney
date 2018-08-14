package ru.popov.bodya.howmoney.presentation.mvp.wallet

import com.arellomobile.mvp.viewstate.strategy.*
import ru.popov.bodya.core.mvp.AppView
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet

interface WalletView : AppView {
    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showWallets(wallets: List<Wallet>)

    fun showTransactions(transactions: List<Transaction>)
    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showSumOfAllIncomeTransactions(sum: Double)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showSumOfAllExpenseTransactions(sum: Double)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showExchangedToUsdBalance(balance: Double)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showExchangedToEurBalance(balance: Double)
}