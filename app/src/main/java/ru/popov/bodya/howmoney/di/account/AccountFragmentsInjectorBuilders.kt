package ru.popov.bodya.howmoney.di.account

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.popov.bodya.howmoney.di.common.modules.SettingsModule
import ru.popov.bodya.howmoney.presentation.ui.about.fragments.AboutFragment
import ru.popov.bodya.howmoney.presentation.ui.addtransaction.AddOperationFragment
import ru.popov.bodya.howmoney.presentation.ui.addtransaction.AddTransactionFragment
import ru.popov.bodya.howmoney.presentation.ui.addtransaction.addtemplate.AddTemplateFragment
import ru.popov.bodya.howmoney.presentation.ui.addwallet.AddWalletFragment
import ru.popov.bodya.howmoney.presentation.ui.settings.fragments.SettingsFragment
import ru.popov.bodya.howmoney.presentation.ui.stats.StatsFragment
import ru.popov.bodya.howmoney.presentation.ui.wallet.WalletFragment

/**
 *  @author popovbodya
 */
@Module
interface AccountFragmentsInjectorBuilders {
    @ContributesAndroidInjector
    fun provideAboutFragment(): AboutFragment

    @ContributesAndroidInjector(modules = [SettingsModule::class])
    fun provideSettingFragment(): SettingsFragment

    @ContributesAndroidInjector
    fun provideWalletFragment(): WalletFragment

    @ContributesAndroidInjector
    fun provideStatsFragment(): StatsFragment

    @ContributesAndroidInjector
    fun provideAddWalletFragment(): AddWalletFragment

    @ContributesAndroidInjector
    fun provideAddOperationFragment() : AddOperationFragment

    @ContributesAndroidInjector
    fun provideAddTemplateFragment(): AddTemplateFragment

    @ContributesAndroidInjector
    fun provideAddTransactionFragment(): AddTransactionFragment
}