package ru.popov.bodya.howmoney.presentation.mvp.settings

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.popov.bodya.core.mvp.AppView

interface SettingsView : AppView {
    fun showFavCurrency(currencyKey: String)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showSuccessDeletionResult()
}