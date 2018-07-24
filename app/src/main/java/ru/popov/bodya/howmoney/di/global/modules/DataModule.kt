package ru.popov.bodya.howmoney.di.global.modules

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import ru.popov.bodya.core.dagger.ApplicationContext
import ru.popov.bodya.howmoney.data.database.preferences.SharedPreferencesWrapper

/**
 *  @author popovbodya
 */
@Module
class DataModule {

    @Provides
    fun provideSharedPreferencesWrapper(sharedPreferences: SharedPreferences): SharedPreferencesWrapper =
            SharedPreferencesWrapper(sharedPreferences)

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

}