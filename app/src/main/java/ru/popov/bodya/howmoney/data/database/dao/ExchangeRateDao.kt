package ru.popov.bodya.howmoney.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Single
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.domain.wallet.models.ExchangeRate

@Dao
interface ExchangeRateDao : BaseDao<ExchangeRate> {
    @Query("SELECT rate FROM exchange_rates WHERE fromCurrency=:fromCurrency AND toCurrency=:toCurrency")
    fun getExchangeRate(fromCurrency: Currency, toCurrency: Currency): Single<Double>
}