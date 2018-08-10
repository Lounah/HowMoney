package ru.popov.bodya.howmoney.presentation.mvp.settings

import ru.popov.bodya.core.mvp.AppView

interface SettingsView : AppView {
    fun showFavCurrency(currencyKey: String)
}