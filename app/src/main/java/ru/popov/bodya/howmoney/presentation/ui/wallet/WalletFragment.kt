package ru.popov.bodya.howmoney.presentation.ui.wallet


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_wallet.*
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet
import ru.popov.bodya.howmoney.presentation.mvp.wallet.WalletPresenter
import ru.popov.bodya.howmoney.presentation.mvp.wallet.WalletView
import ru.popov.bodya.howmoney.presentation.ui.account.activities.AccountActivity
import ru.popov.bodya.howmoney.presentation.ui.common.BaseFragment
import ru.popov.bodya.howmoney.presentation.ui.common.Screens
import ru.popov.bodya.howmoney.presentation.util.ZoomOutPageTransformer
import java.text.DecimalFormat
import javax.inject.Inject

class WalletFragment : BaseFragment(), WalletView {

    private companion object {
        const val SELECTED_TAB_KEY = "SELECTED_TAB_KEY"
    }

    override val TAG: String
        get() = Screens.WALLET_SCREEN
    override val layoutRes: Int
        get() = R.layout.fragment_wallet
    override val toolbarTitleId: Int
        get() = R.string.wallet

    private val formatter = DecimalFormat("#0.00")

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
        if (wallets.isNotEmpty())
            walletPresenter.fetchTransactionsSumByWalletId(wallets[vp_amount.currentItem].id)
        pagerAdapter.updateDataSet(wallets)
    }

    override fun showTransactions(transactions: List<Transaction>) {
        transactionsAdapter.updateDataSet(transactions)
    }

    override fun showFirstFavExchangeRate(rate: Double) {
        currency_top_exchange_rate.text = formatter.format(rate)
        setShouldShowCurrencySpinner(false)
    }

    override fun showSecondFavExchangeRate(rate: Double) {
        currency_bottom_exchange_rate.text = formatter.format(rate)
        setShouldShowCurrencySpinner(false)
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
        initCurrenciesRadioGroup()
        initTransactionsList()
        initWalletViewPager()
    }

    private fun initFab() {
        fab_new_income.setOnClickListener { addNewIncome() }
        fab_new_expense.setOnClickListener { addNewExpense() }
    }

    private fun addNewIncome() {
        walletPresenter.onAddNewIncomeTransactionFabClick()
    }

    private fun addNewExpense() {
        walletPresenter.onAddNewExpenseTransactionFabClick()
    }

    private fun initCurrenciesRadioGroup() {
        rg_currencies.check(R.id.btn_rub)
        rg_currencies.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) { // TODO: CONVERT CURRENT AMOUNT TO NEW CURRENCY}
            }
        }
    }

    private fun initWalletViewPager() {
        vp_amount.setPageTransformer(true, ZoomOutPageTransformer())
        pagerAdapter = WalletVPAdapter()
        vp_amount.adapter = pagerAdapter
        tl_dots.setupWithViewPager(vp_amount, true)
        vp_amount.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                walletPresenter.fetchTransactionsSumByWalletId(accountWallets[position].id)
            }
        })
    }

    private fun initTransactionsList() {
        transactionsAdapter = TransactionsRVAdapter()
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

        rv_transactions.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 20 && fab_add.visibility == View.VISIBLE) {
                    hideFab()
                }
                if (dy < -20 && fab_add.visibility != View.VISIBLE) {
                    showFab()
                }
            }
        })
    }

    private fun hideFab() {
        fab_add.collapse()
        fab_add.visibility = View.GONE
    }

    private fun showFab() {
        fab_add.visibility = View.VISIBLE
    }

    private fun setShouldShowCurrencySpinner(value: Boolean) {
        when (value) {
            true -> {
                pb_placeholder.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
            }
            false -> {
                pb_placeholder.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }
    }
}