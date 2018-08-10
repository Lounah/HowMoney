package ru.popov.bodya.howmoney.data.repositories

import io.reactivex.Single
import ru.popov.bodya.howmoney.data.database.dao.ExchangeRateDao
import ru.popov.bodya.howmoney.data.network.api.CurrenciesRateApiWrapper
import ru.popov.bodya.howmoney.data.network.beans.CurrentRateBean
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.domain.wallet.models.ExchangeRate

class CurrencyRateRepository(private val currenciesRateApiWrapper: CurrenciesRateApiWrapper,
                             private val exchangeRateDao: ExchangeRateDao) {
    fun getExchangeRate(fromCurrency: Currency, toCurrency: Currency): Single<Double> =
            getExchangeRateFromApi(fromCurrency, toCurrency)
                    .doOnSuccess {
                        exchangeRateDao.insert(ExchangeRate(fromCurrency, toCurrency, it.result, it.date))
                    }.map { it -> it.result }
                    .onErrorResumeNext { exchangeRateDao.getExchangeRate(fromCurrency, toCurrency) }

    private fun getExchangeRateFromApi(fromCurrency: Currency, toCurrency: Currency): Single<CurrentRateBean>
            = currenciesRateApiWrapper.getCurrentRate(fromCurrency, toCurrency)
}