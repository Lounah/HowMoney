package ru.popov.bodya.howmoney.domain.wallet.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date
@Parcelize
@Entity(tableName = "transactions")
data class Transaction(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                       val currency: Currency = Currency.RUB,
                       var amount: Double = 0.0,
                       val category: Category = Category.OTHER,
                       val walletId: Int = 0,
                       val comment: String = "",
                       val template: Boolean = false,
                       val periodic: Boolean = false,
                       val period: Long? = 0,
                       val date: Date) : Parcelable

enum class Category {
    FOOD, CLOTHES, COMMUNAL_PAYMENTS, REST,
    EDUCATION, HOME, FAMILY, AUTO, TREATMENT, SALARY, OTHER
}

class TransactionCategoryConverter {

    @TypeConverter
    fun fromTransactionCategoryToInt(category: Category): Int =
            when (category) {
                Category.FOOD -> 12
                Category.HOME -> 13
                Category.COMMUNAL_PAYMENTS -> 14
                Category.EDUCATION -> 15
                Category.CLOTHES -> 16
                Category.FAMILY -> 17
                Category.REST -> 18
                Category.TREATMENT -> 19
                Category.AUTO -> 20
                Category.OTHER -> 21
                Category.SALARY -> 22
            }

    @TypeConverter
    fun fromIntToTransactionCategory(category: Int): Category =
            when (category) {
                12 -> Category.FOOD
                13 -> Category.HOME
                14 -> Category.COMMUNAL_PAYMENTS
                15 -> Category.EDUCATION
                16 -> Category.CLOTHES
                17 -> Category.FAMILY
                18 -> Category.REST
                19 -> Category.TREATMENT
                20 -> Category.AUTO
                21 -> Category.OTHER
                22 -> Category.SALARY
                else -> Category.OTHER
            }
}

class TimeStampConverter {
    @TypeConverter
    fun fromDate(date: Date) = date.time

    @TypeConverter
    fun toDate(millisSinceEpoch: Long) = Date(millisSinceEpoch)
}