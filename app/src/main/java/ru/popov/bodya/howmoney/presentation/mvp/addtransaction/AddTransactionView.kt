package ru.popov.bodya.howmoney.presentation.mvp.addtransaction

import ru.popov.bodya.core.mvp.AppView
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet

interface AddTransactionView : AppView {
    fun onTransactionCreated()
    fun showWallets(wallets: List<Wallet>)
}