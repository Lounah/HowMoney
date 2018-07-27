package ru.popov.bodya.howmoney.presentation.mvp.account

import ru.popov.bodya.core.mvp.AppView

/**
 *  @author popovbodya
 */
interface AccountView : AppView {

    fun showRUBAmount(amount: Double)

    fun showUSDAmount(amount: Double)
}