package ru.popov.bodya.howmoney.di.account

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.popov.bodya.howmoney.data.database.AppDatabase
import ru.popov.bodya.howmoney.data.database.dao.ExchangeRateDao
import ru.popov.bodya.howmoney.data.database.dao.TransactionsDao
import ru.popov.bodya.howmoney.data.database.dao.WalletDao
import ru.popov.bodya.howmoney.data.network.api.CurrenciesRateApi
import ru.popov.bodya.howmoney.data.network.api.CurrenciesRateApi.Companion.FIXER_BASE_URL
import ru.popov.bodya.howmoney.data.network.api.CurrenciesRateApiWrapper
import ru.popov.bodya.howmoney.data.repositories.CurrencyRateRepository
import ru.popov.bodya.howmoney.data.repositories.TransactionsRepository
import ru.popov.bodya.howmoney.data.repositories.WalletRepository
import ru.popov.bodya.howmoney.data.util.RateLimiter
import ru.popov.bodya.howmoney.di.common.modules.SettingsModule
import java.util.concurrent.TimeUnit

@Module(includes = [SettingsModule::class])
class AccountRepositoryModule {
    @Provides
    fun provideWalletRepository(walletDao: WalletDao): WalletRepository
            = WalletRepository(walletDao)

    @Provides
    fun provideTransactionsRepository(transactionsDao: TransactionsDao): TransactionsRepository
            = TransactionsRepository(transactionsDao)

    @Provides
    fun provideCurrencyRepository(currenciesRateApiWrapper: CurrenciesRateApiWrapper, exchangeRateDao: ExchangeRateDao): CurrencyRateRepository =
            CurrencyRateRepository(currenciesRateApiWrapper, exchangeRateDao)

    @Provides
    fun provideCurrenciesRateApiWrapper(currenciesRateApi: CurrenciesRateApi): CurrenciesRateApiWrapper =
            CurrenciesRateApiWrapper(currenciesRateApi)

    @Provides
    fun provideExchangeRateApi(okHttpClient: OkHttpClient,
                               converterFactory: Converter.Factory,
                               adapterFactory: CallAdapter.Factory): CurrenciesRateApi =
            Retrofit.Builder()
                    .baseUrl(FIXER_BASE_URL)
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(adapterFactory)
                    .client(okHttpClient)
                    .build()
                    .create(CurrenciesRateApi::class.java)

    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Provides
    fun provideWalletDao(db: AppDatabase): WalletDao = db.walletDao

    @Provides
    fun provideTransactionsDao(db: AppDatabase): TransactionsDao = db.transactionsDao

    @Provides
    fun provideExchangeRateDao(db: AppDatabase): ExchangeRateDao = db.exchangeRateDao


    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

    @Provides
    internal fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }
}