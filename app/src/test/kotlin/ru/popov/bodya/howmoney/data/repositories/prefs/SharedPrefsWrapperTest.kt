package ru.popov.bodya.howmoney.data.repositories.prefs

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import ru.popov.bodya.howmoney.data.database.preferences.SharedPreferencesWrapper

@RunWith(RobolectricTestRunner::class)
class SharedPrefsWrapperTest {

    @Test
    fun shouldSaveCurrencyInPrefs() {
        val prefs = RuntimeEnvironment.application.getSharedPreferences("default", 0)
        val prefsWrapper = SharedPreferencesWrapper(prefs)

        prefsWrapper.setNewFavCurrency("RUB")

        assertThat(prefs.getString("FAV_CURRENCY_KEY", "NAN"), equalTo("RUB"))
    }
}