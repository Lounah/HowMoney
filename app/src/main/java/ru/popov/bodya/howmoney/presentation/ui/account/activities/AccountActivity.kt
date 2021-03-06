package ru.popov.bodya.howmoney.presentation.ui.account.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import ru.popov.bodya.core.mvp.AppActivity
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet
import ru.popov.bodya.howmoney.presentation.mvp.account.AccountPresenter
import ru.popov.bodya.howmoney.presentation.mvp.account.AccountView
import ru.popov.bodya.howmoney.presentation.ui.about.fragments.AboutFragment
import ru.popov.bodya.howmoney.presentation.ui.addtransaction.AddOperationFragment
import ru.popov.bodya.howmoney.presentation.ui.addtransaction.AddTransactionFragment
import ru.popov.bodya.howmoney.presentation.ui.common.Screens.ABOUT_SCREEN
import ru.popov.bodya.howmoney.presentation.ui.common.Screens.NEW_TRANSACTION_SCREEN
import ru.popov.bodya.howmoney.presentation.ui.common.Screens.SETTINGS_SCREEN
import ru.popov.bodya.howmoney.presentation.ui.common.Screens.STATS_SCREEN
import ru.popov.bodya.howmoney.presentation.ui.common.Screens.WALLET_SCREEN
import ru.popov.bodya.howmoney.presentation.ui.settings.fragments.SettingsFragment
import ru.popov.bodya.howmoney.presentation.ui.stats.StatsFragment
import ru.popov.bodya.howmoney.presentation.ui.wallet.WalletFragment
import ru.popov.bodya.howmoney.presentation.ui.addwallet.AddWalletFragment
import ru.popov.bodya.howmoney.presentation.ui.common.Screens.PERIODIC_SCREEN
import ru.popov.bodya.howmoney.presentation.ui.pendings.PendingTransactionsFragment
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import javax.inject.Inject


class AccountActivity : AppActivity(), AccountView,
        NavigationView.OnNavigationItemSelectedListener,
        AddWalletFragment.OnWalletCreatedCallback,
        HasSupportFragmentInjector {
    @Inject
    @InjectPresenter
    lateinit var accountPresenter: AccountPresenter
    @Inject
    lateinit var navigationHolder: NavigatorHolder
    @Inject
    lateinit var injector: DispatchingAndroidInjector<Fragment>

    private lateinit var addWalletFragment: AddWalletFragment

    @ProvidePresenter
    fun provideOverviewPresenter(): AccountPresenter = accountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    override fun onResume() {
        super.onResume()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                accountPresenter.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        if (supportFragmentManager.backStackEntryCount == 0) {
            onShowMenuItem(R.id.add_wallet)
        } else onHideMenuItem(R.id.add_wallet)
        return true
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = injector

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_wallet -> {
                nav_view.setCheckedItem(R.id.nav_wallet)
                accountPresenter.navigateToWalletScreen()
            }
            R.id.nav_settings -> {
                nav_view.setCheckedItem(R.id.nav_settings)
                accountPresenter.navigateToSettingsScreen()
            }
            R.id.nav_stats -> {
                nav_view.setCheckedItem(R.id.nav_stats)
                accountPresenter.navigateToStatsScreen()
            }
            R.id.nav_periodic -> {
                nav_view.setCheckedItem(R.id.nav_periodic)
                accountPresenter.navigateToPeriodicTransactionsScreen()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.add_wallet -> {
                addWalletFragment = AddWalletFragment()
                addWalletFragment.show(supportFragmentManager, "ADD_WALLET_FRAG")
            }
        }
        return true
    }

    override fun onWalletComposed(wallet: Wallet) {
        accountPresenter.createWallet(wallet)
    }

    override fun onWalletCreated() {
        addWalletFragment.dismiss()
        Toast.makeText(this, R.string.wallet_successfully_created, Toast.LENGTH_SHORT).show()
    }

    fun updateToolBar(titleResId: Int) {
        toolbar.title = getString(titleResId)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        if (supportFragmentManager.backStackEntryCount == 0) {
            initToggle()
            onShowMenuItem(R.id.add_wallet)
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            onHideMenuItem(R.id.add_wallet)
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            setBackArrow(true)
        }
    }

    private fun initUI() {
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_wallet)
        toolbar.title = resources.getString(R.string.wallet)
        setSupportActionBar(toolbar)
        initToggle()
    }

    private fun onShowMenuItem(resId: Int) {
        toolbar.menu.findItem(resId)?.isVisible = true
        invalidateOptionsMenu()
    }

    private fun onHideMenuItem(resId: Int) {
        toolbar.menu.findItem(resId)?.isVisible = false
        invalidateOptionsMenu()
    }

    private fun setBackArrow(state: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(state)
    }

    private fun initToggle() {
        setBackArrow(false)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private val navigator = object : SupportAppNavigator(this, supportFragmentManager, R.id.main_container) {
        override fun createActivityIntent(context: Context, screenKey: String, data: Any?): Intent? {
            return null
        }

        override fun createFragment(screenKey: String, data: Any?): Fragment? {
            return when (screenKey) {
                WALLET_SCREEN -> WalletFragment()
                STATS_SCREEN -> StatsFragment()
                NEW_TRANSACTION_SCREEN -> AddOperationFragment()
                SETTINGS_SCREEN -> SettingsFragment()
                ABOUT_SCREEN -> AboutFragment()
                PERIODIC_SCREEN -> PendingTransactionsFragment()
                else -> null
            }
        }

        override fun showSystemMessage(message: String) {
            Toast.makeText(this@AccountActivity, message, Toast.LENGTH_SHORT).show()
        }

        override fun exit() {
            finish()
        }
    }
}