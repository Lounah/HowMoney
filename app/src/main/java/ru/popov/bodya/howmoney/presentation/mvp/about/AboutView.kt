package ru.popov.bodya.howmoney.presentation.mvp.about

import ru.popov.bodya.core.mvp.AppView

/**
 *  @author popovbodya
 */
interface AboutView : AppView {
    fun showAppVersion(version: String)
}