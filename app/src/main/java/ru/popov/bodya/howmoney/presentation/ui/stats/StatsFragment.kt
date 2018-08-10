package ru.popov.bodya.howmoney.presentation.ui.stats

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_stats.*
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Stats
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet
import ru.popov.bodya.howmoney.presentation.mvp.stats.StatsPresenter
import ru.popov.bodya.howmoney.presentation.mvp.stats.StatsView
import ru.popov.bodya.howmoney.presentation.ui.account.activities.AccountActivity
import ru.popov.bodya.howmoney.presentation.ui.common.BaseFragment
import ru.popov.bodya.howmoney.presentation.ui.wallet.WalletVPAdapter
import ru.popov.bodya.howmoney.presentation.util.view.ZoomOutPageTransformer
import javax.inject.Inject


class StatsFragment : BaseFragment(), StatsView {
    override val TAG: String
        get() = "STATS_FRAGMENT"
    override val layoutRes: Int
        get() = R.layout.fragment_stats
    override val toolbarTitleId: Int
        get() = R.string.statistics

    private lateinit var accountWallets: List<Wallet>


    private lateinit var pagerAdapter: WalletVPAdapter

    private lateinit var statsAdapter: StatsRvAdapter


    @Inject
    @InjectPresenter
    lateinit var statsPresenter: StatsPresenter

    @ProvidePresenter
    fun provideStatsPresenter(): StatsPresenter = statsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun setUpToolbarTitle(resId: Int) {
        (activity as AccountActivity).updateToolBar(resId)
    }

    private fun initUI() {
        initWalletViewPager()
        initTransactionsList()
    }

    override fun showWallets(wallets: List<Wallet>) {
        accountWallets = wallets
        statsPresenter.fetchTransactionsSumByWalletId(wallets[vp_amount.currentItem].id)
        statsPresenter.exchangeBalance(accountWallets[vp_amount.currentItem].majorCurrency,
                accountWallets[vp_amount.currentItem].amount)
        statsPresenter.fetchStats(accountWallets[vp_amount.currentItem].id)
        pagerAdapter.updateDataSet(wallets)
    }

    override fun showStatsByCategory(stats: List<Stats>) {
        statsAdapter.updateDataSet(stats)
    }

    override fun showSumOfAllIncomeTransactions(sum: Double) {
        tv_incomes.amount = sum.toFloat()
    }

    override fun showSumOfAllExpenseTransactions(sum: Double) {
        tv_expenses.amount = sum.toFloat()
    }

    override fun showExchangedToUsdBalance(balance: Double) {
        tv_usd_amount.amount = balance.toFloat()
    }

    override fun showExchangedToEurBalance(balance: Double) {
        tv_eur_amount.amount = balance.toFloat()
    }

    private fun initWalletViewPager() {
        vp_amount.setPageTransformer(true, ZoomOutPageTransformer())
        pagerAdapter = WalletVPAdapter()
        vp_amount.adapter = pagerAdapter
        tl_dots.setupWithViewPager(vp_amount, true)
        vp_amount.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                statsPresenter.onWalletChanged(accountWallets[position].id)
                statsPresenter.exchangeBalance(accountWallets[position].majorCurrency, accountWallets[position].amount)
            }
        })
    }

    private fun initTransactionsList() {
        statsAdapter = StatsRvAdapter()
        rv_stats.adapter = statsAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        rv_stats.layoutManager = linearLayoutManager
    }

}