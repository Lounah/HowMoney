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
import javax.inject.Inject


/**
 *  @author popovbodya
 */
class AboutFragment : AppFragment(), AboutView {

    @Inject
    @InjectPresenter
    lateinit var aboutPresenter: AboutPresenter

    @ProvidePresenter
    fun provideAboutPresenter(): AboutPresenter = aboutPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_about_app, container, false)
        setHasOptionsMenu(true)
        return view
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

    override fun showAppVersion(version: String) {
        tv_app_version.text = version
    }
}