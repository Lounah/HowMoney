package ru.popov.bodya.howmoney.presentation.ui.wallet


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.popov.bodya.howmoney.domain.wallet.models.Type
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet
import ru.popov.bodya.howmoney.presentation.mvp.wallet.WalletPresenter
import ru.popov.bodya.howmoney.presentation.mvp.wallet.WalletView
import ru.popov.bodya.howmoney.presentation.ui.account.activities.AccountActivity
import ru.popov.bodya.howmoney.presentation.ui.common.BaseFragment
import ru.popov.bodya.howmoney.presentation.ui.common.Screens
import ru.popov.bodya.howmoney.presentation.util.view.ZoomOutPageTransformer
import java.text.DecimalFormat
import javax.inject.Inject

class WalletFragment : BaseFragment(), WalletView,
        OptionsDialogFragment.OnClickListener {

    private companion object {
        const val SELECTED_TAB_KEY = "SELECTED_TAB_KEY"
    }

    override val TAG: String
        get() = Screens.WALLET_SCREEN
    override val layoutRes: Int
        get() = R.layout.fragment_wallet
    override val toolbarTitleId: Int
        get() = R.string.wallet

    private lateinit var accountWallets: List<Wallet>

    @Inject
    @InjectPresenter
    lateinit var walletPresenter: WalletPresenter

    @ProvidePresenter
    fun provideWalletPresenter(): WalletPresenter = walletPresenter

    private lateinit var pagerAdapter: WalletVPAdapter
    private lateinit var transactionsAdapter: TransactionsRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun showWallets(wallets: List<Wallet>) {
        accountWallets = wallets
        if (wallets.isNotEmpty()) {
            walletPresenter.fetchTransactionsSumByWalletId(wallets[vp_amount.currentItem].id)
            walletPresenter.exchangeBalance(accountWallets[vp_amount.currentItem].majorCurrency, accountWallets[vp_amount.currentItem].amount)
            pagerAdapter.updateDataSet(wallets)
        } else walletPresenter.fetchAllWallets()
    }

    override fun showTransactions(transactions: List<Transaction>) {
        transactionsAdapter.updateDataSet(transactions)
    }

    override fun showSumOfAllIncomeTransactions(sum: Double) {
        tv_incomes.amount = sum.toFloat()
    }

    override fun showSumOfAllExpenseTransactions(sum: Double) {
        tv_expenses.amount = sum.toFloat()
    }

    override fun setUpToolbarTitle(resId: Int) {
        (activity as AccountActivity).updateToolBar(resId)
    }

    private fun initUI() {
        initFab()
        initTransactionsList()
        initWalletViewPager()
    }

    private fun initFab() {
        fab_add.setOnClickListener { walletPresenter.onFabAddClicked() }
    }

    override fun showExchangedToUsdBalance(balance: Double) {
        tv_usd_amount.amount = balance.toFloat()
    }

    override fun showExchangedToEurBalance(balance: Double) {
        tv_eur_amount.amount = balance.toFloat()
    }

    override fun onDeleteTransactionClicked(transaction: Transaction) {
        walletPresenter.deleteTransaction(transaction)
    }

    private fun initWalletViewPager() {
        vp_amount.setPageTransformer(true, ZoomOutPageTransformer())
        pagerAdapter = WalletVPAdapter()
        vp_amount.adapter = pagerAdapter
        tl_dots.setupWithViewPager(vp_amount, true)
        vp_amount.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                walletPresenter.onWalletChanged(accountWallets[position].id)
                walletPresenter.exchangeBalance(accountWallets[position].majorCurrency, accountWallets[position].amount)
            }
        })
    }

    private fun initTransactionsList() {
        transactionsAdapter = TransactionsRVAdapter(object : TransactionsRVAdapter.OnLongClickCallback {
            override fun onLongClicked(item: Transaction) {
                showDialogFrag(item)
            }
        })
        rv_transactions.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        rv_transactions.adapter = transactionsAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        rv_transactions.layoutManager = linearLayoutManager

        tl_transaction_types.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> walletPresenter.fetchAllTransactions()
                    1 -> walletPresenter.fetchAllIncomeTransactions()
                    2 -> walletPresenter.fetchAllExpenseTransactions()
                }
            }
        })
    }

    private fun showDialogFrag(transaction: Transaction) {
        val optionsDialog = OptionsDialogFragment.newInstance(transaction)
        optionsDialog.show(childFragmentManager, TAG)
    }
}