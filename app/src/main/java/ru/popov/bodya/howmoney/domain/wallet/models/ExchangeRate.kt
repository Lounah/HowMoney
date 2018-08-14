package ru.popov.bodya.howmoney.domain.wallet.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRate(@PrimaryKey val fromCurrency: Currency,
                        val toCurrency: Currency,
                        val rate: Double = 0.0,
                        val date: String)