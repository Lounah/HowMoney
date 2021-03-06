package ru.popov.bodya.howmoney.presentation.mvp.about

import android.content.pm.PackageManager
import com.arellomobile.mvp.InjectViewState
import ru.popov.bodya.core.mvp.AppPresenter
import ru.popov.bodya.core.resources.ResourceManager
import ru.popov.bodya.howmoney.BuildConfig
import ru.popov.bodya.howmoney.R
import ru.terrakok.cicerone.Router
import javax.inject.Inject


/**
 *  @author popovbodya
 */
@InjectViewState
class AboutPresenter @Inject constructor(
        private val router: Router,
        private val resourceManager: ResourceManager
) : AppPresenter<AboutView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showAppVersion(getAppVersion())
    }

    fun onUpButtonPressed() {
        router.exit()
    }

    private fun getAppVersion(): String =
            resourceManager.getString(R.string.version_code, BuildConfig.VERSION_NAME)
}