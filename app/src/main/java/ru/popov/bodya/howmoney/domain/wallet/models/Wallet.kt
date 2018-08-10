package ru.popov.bodya.howmoney.domain.wallet.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter

@Entity(tableName = "wallets")
data class Wallet(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                  var amount: Double = 0.0,
                  val type: Type,
                  val majorCurrency: Currency,
                  val name: String = "")

enum class Type {
    CASH, DEBIT_CARD, BANK_ACCOUNT
}

class WalletTypeConverter {

    @TypeConverter
    fun fromType(type: Type): Int =
            when (type) {
                Type.BANK_ACCOUNT -> 0
                Type.CASH -> 1
                Type.DEBIT_CARD -> 2
            }

    @TypeConverter
    fun toCurrency(type: Int): Type =
            when (type) {
                0 -> Type.BANK_ACCOUNT
                1 -> Type.CASH
                2 -> Type.DEBIT_CARD
                else -> Type.CASH
            }
}