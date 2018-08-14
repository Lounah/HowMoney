package ru.popov.bodya.howmoney.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import ru.popov.bodya.howmoney.di.common.AppInjector
import javax.inject.Inject

/**
 *  @author popovbodya
 */
class HowMoneyApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector
}