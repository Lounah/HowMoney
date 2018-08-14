package ru.popov.bodya.howmoney.data.database.preferences

import android.content.SharedPreferences

/**
 *  @author popovbodya
 */
class SharedPreferencesWrapper(private val sharedPreferences: SharedPreferences) {

    private companion object {
        const val FAV_CURRENCY_KEY = "FAV_CURRENCY_KEY"
        const val DEFAULT_CURRENCY = "RUB"
    }

    fun getFavExchangeRate(): String = sharedPreferences.getString(FAV_CURRENCY_KEY, DEFAULT_CURRENCY)

    fun setNewFavCurrency(currency: String) {
        val editor = sharedPreferences.edit()
        editor.putString(FAV_CURRENCY_KEY, currency)
        editor.apply()
    }
}