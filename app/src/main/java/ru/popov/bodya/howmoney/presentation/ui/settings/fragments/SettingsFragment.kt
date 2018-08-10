package ru.popov.bodya.howmoney.presentation.ui.settings.fragments

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.presentation.mvp.settings.SettingsPresenter
import ru.popov.bodya.howmoney.presentation.mvp.settings.SettingsView
import ru.popov.bodya.howmoney.presentation.ui.account.activities.AccountActivity
import ru.popov.bodya.howmoney.presentation.ui.common.BaseFragment
import ru.popov.bodya.howmoney.presentation.ui.common.Screens
import javax.inject.Inject

class SettingsFragment : BaseFragment(), SettingsView {

    override val toolbarTitleId: Int
        get() = R.string.settings
    override val TAG: String
        get() = Screens.SETTINGS_SCREEN
    override val layoutRes: Int
        get() = R.layout.fragment_settings

    @Inject
    @InjectPresenter
    lateinit var settingsPresenter: SettingsPresenter

    @ProvidePresenter
    fun provideSettingsPresenter(): SettingsPresenter = settingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun setUpToolbarTitle(resId: Int) {
        (activity as AccountActivity).updateToolBar(resId)
    }

    override fun showFavCurrency(currencyKey: String) {
        when(currencyKey) {
            "RUB" -> rg_currencies.check(R.id.btn_rub)
            "USD" -> rg_currencies.check(R.id.btn_usd)
            "EUR" -> rg_currencies.check(R.id.btn_eur)
        }
    }

    private fun initUI() {
        rg_currencies.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.btn_rub -> settingsPresenter.updateFavCurrency("RUB")
                R.id.btn_usd -> settingsPresenter.updateFavCurrency("USD")
                R.id.btn_eur -> settingsPresenter.updateFavCurrency("EUR")
            }
        }
        tv_about.setOnClickListener { settingsPresenter.navigateToAboutScreen() }
    }
}