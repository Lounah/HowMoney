package ru.popov.bodya.howmoney.domain.wallet.models

import android.arch.persistence.room.TypeConverter

enum class Currency {
    RUB, EUR, USD
}

class CurrencyTypeConverter {

    @TypeConverter
    fun fromType(type: Currency): Int =
            when (type) {
                Currency.EUR -> 0
                Currency.USD -> 1
                Currency.RUB -> 2
            }

    @TypeConverter
    fun toCurrency(type: Int): Currency =
            when (type) {
                0 -> Currency.EUR
                1 -> Currency.USD
                2 -> Currency.RUB
                else -> Currency.RUB
            }
}