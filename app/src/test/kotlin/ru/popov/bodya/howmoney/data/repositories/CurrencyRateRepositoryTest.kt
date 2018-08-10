package ru.popov.bodya.howmoney.data.repositories

import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import ru.popov.bodya.howmoney.data.database.dao.ExchangeRateDao
import ru.popov.bodya.howmoney.data.network.api.CurrenciesRateApiWrapper
import ru.popov.bodya.howmoney.data.network.beans.CurrentRateBean
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.domain.wallet.models.ExchangeRate

@RunWith(JUnit4::class)
class CurrencyRateRepositoryTest {

    private lateinit var currenciesRateApiWrapper: CurrenciesRateApiWrapper
    private lateinit var currencyRateRepository: CurrencyRateRepository
    private lateinit var exchangeRateDao: ExchangeRateDao

    private val fromCurrency = Currency.RUB
    private val toCurrency = Currency.USD

    @Before
    fun setUp() {
        currenciesRateApiWrapper = mock(CurrenciesRateApiWrapper::class.java)
        exchangeRateDao = mock(ExchangeRateDao::class.java)
        currencyRateRepository = CurrencyRateRepository(currenciesRateApiWrapper, exchangeRateDao)
    }

    @Test
    fun exchangeRate_getsSuccessfully() {
        val expected = CurrentRateBean("23.06.1999", 29.0)
        `when`(currenciesRateApiWrapper.getCurrentRate(fromCurrency, toCurrency))
                .thenReturn(Single.just(expected))

        currencyRateRepository.getExchangeRate(fromCurrency, toCurrency)
                .test()
                .assertValue(expected.result)

        verify(currenciesRateApiWrapper).getCurrentRate(fromCurrency, toCurrency)
        verifyNoMoreInteractions(currenciesRateApiWrapper)
        verify(exchangeRateDao).insert(ExchangeRate(fromCurrency, toCurrency, expected.result, expected.date))
        verifyNoMoreInteractions(exchangeRateDao)
    }

    @Test
    fun exchangeRate_getsWithError() {
        val expectedException = RuntimeException()
        `when`(currenciesRateApiWrapper.getCurrentRate(fromCurrency, toCurrency))
                .thenReturn(Single.error(expectedException))

        currencyRateRepository.getExchangeRate(fromCurrency, toCurrency)
                .test()
                .assertError(expectedException)

        verify(currenciesRateApiWrapper).getCurrentRate(fromCurrency, toCurrency)
        verifyNoMoreInteractions(currenciesRateApiWrapper)
        verify(exchangeRateDao).getExchangeRate(fromCurrency, toCurrency)
    }
}