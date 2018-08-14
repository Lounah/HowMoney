package ru.popov.bodya.howmoney.presentation.mvp.periodic

import ru.popov.bodya.core.mvp.AppView
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction

interface PeriodicTransactionsView : AppView {
    fun showTransactions(periodicTransactions: List<Transaction>)
}