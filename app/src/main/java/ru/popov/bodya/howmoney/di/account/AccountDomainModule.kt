package ru.popov.bodya.howmoney.di.account

import dagger.Module
import dagger.Provides
import ru.popov.bodya.howmoney.data.repositories.CurrencyRateRepository
import ru.popov.bodya.howmoney.data.repositories.TransactionsRepository
import ru.popov.bodya.howmoney.data.repositories.WalletRepository
import ru.popov.bodya.howmoney.domain.launch.LaunchInteractor
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor


@Module
class AccountDomainModule {
    @Provides
    fun provideLaunchInteractor(walletRepository: WalletRepository)  =
            LaunchInteractor(walletRepository)

    @Provides
    fun provideWalletInteractor(currencyRateRepository: CurrencyRateRepository,
                                walletRepository: WalletRepository,
                                transactionsRepository: TransactionsRepository)
            = WalletInteractor(currencyRateRepository, walletRepository, transactionsRepository)
}