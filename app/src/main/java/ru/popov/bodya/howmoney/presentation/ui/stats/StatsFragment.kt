package ru.popov.bodya.howmoney.presentation.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dagger.android.support.AndroidSupportInjection
import ru.popov.bodya.core.mvp.AppFragment
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.presentation.mvp.stats.StatsPresenter
import ru.popov.bodya.howmoney.presentation.mvp.stats.StatsView
import javax.inject.Inject


class StatsFragment : AppFragment(), StatsView {

    @Inject
    @InjectPresenter
    lateinit var statsPresenter: StatsPresenter

    @ProvidePresenter
    fun provideStatsPresenter(): StatsPresenter = statsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }
}