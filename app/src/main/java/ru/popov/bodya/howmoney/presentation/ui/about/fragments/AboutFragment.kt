package ru.popov.bodya.howmoney.presentation.ui.about.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_about_app.*
import ru.popov.bodya.core.mvp.AppFragment
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.presentation.mvp.about.AboutPresenter
import ru.popov.bodya.howmoney.presentation.mvp.about.AboutView
import ru.popov.bodya.howmoney.presentation.ui.account.activities.AccountActivity
import ru.popov.bodya.howmoney.presentation.ui.common.BaseFragment
import javax.inject.Inject


/**
 *  @author popovbodya
 */
class AboutFragment : BaseFragment(), AboutView {
    override val TAG: String
        get() = "ABOUT_FRAGMENT"
    override val layoutRes: Int
        get() = R.layout.fragment_about_app
    override val toolbarTitleId: Int
        get() = R.string.about_application

    @Inject
    @InjectPresenter
    lateinit var aboutPresenter: AboutPresenter

    @ProvidePresenter
    fun provideAboutPresenter(): AboutPresenter = aboutPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                aboutPresenter.onUpButtonPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setUpToolbarTitle(resId: Int) {
        (activity as AccountActivity).updateToolBar(resId)
    }

    override fun showAppVersion(version: String) {
        tv_app_version.text = version
    }
}