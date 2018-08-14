package ru.popov.bodya.howmoney.data.network.api

import io.reactivex.Single
import ru.popov.bodya.howmoney.BuildConfig
import ru.popov.bodya.howmoney.data.network.beans.CurrentRateBean
import ru.popov.bodya.howmoney.domain.wallet.models.Currency

class CurrenciesRateApiWrapper(private val currenciesRateApi: CurrenciesRateApi) {

    companion object {
        const val API_KEY = "access_key"
        const val FROM_CURRENCY_KEY = "from"
        const val TO_CURRENCY_KEY = "to"
        const val DEFAULT_AMOUNT_KEY = "amount"
        const val BASE_AMOUNT = "1"
    }

    fun getCurrentRate(fromCurrency: Currency, toCurrency: Currency): Single<CurrentRateBean>
            = currenciesRateApi.getCurrentRate(buildQueryMapForExchangeRate(fromCurrency, toCurrency))


    private fun buildQueryMapForExchangeRate(fromCurrency: Currency, toCurrency: Currency): Map<String, String> {
        return hashMapOf(
                API_KEY to BuildConfig.FIXER_API_KEY,
                FROM_CURRENCY_KEY to fromCurrency.toString(),
                TO_CURRENCY_KEY to toCurrency.toString(),
                DEFAULT_AMOUNT_KEY to BASE_AMOUNT
        )
    }
}