package ru.popov.bodya.howmoney.presentation.mvp.wallet

import ru.popov.bodya.core.mvp.AppView
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet

interface WalletView : AppView {
    fun showWallets(wallets: List<Wallet>)
    fun showTransactions(transactions: List<Transaction>)
    fun showFirstFavExchangeRate(rate: Double)
    fun showSecondFavExchangeRate(rate: Double)
    fun showSumOfAllIncomeTransactions(sum: Double)
    fun showSumOfAllExpenseTransactions(sum: Double)
}