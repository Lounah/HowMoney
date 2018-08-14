package ru.popov.bodya.howmoney.presentation.ui.pendings

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_pending_transactions.*
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.popov.bodya.howmoney.presentation.mvp.periodic.PeriodicTransactionsPresenter
import ru.popov.bodya.howmoney.presentation.mvp.periodic.PeriodicTransactionsView
import ru.popov.bodya.howmoney.presentation.ui.account.activities.AccountActivity
import ru.popov.bodya.howmoney.presentation.ui.common.BaseFragment
import ru.popov.bodya.howmoney.presentation.ui.stats.StatsRvAdapter
import ru.popov.bodya.howmoney.presentation.ui.wallet.OptionsDialogFragment
import ru.popov.bodya.howmoney.presentation.ui.wallet.TransactionsRVAdapter
import javax.inject.Inject

class PendingTransactionsFragment : BaseFragment(), PeriodicTransactionsView, OptionsDialogFragment.OnClickListener {
    override val TAG: String
        get() = "PENDING_TRANSACTIONS_FRAGMENT"
    override val layoutRes: Int
        get() = R.layout.fragment_pending_transactions
    override val toolbarTitleId: Int
        get() = R.string.pending_transactions

    private lateinit var adapter: PeriodicTransactionsRvAdapter

    @Inject
    @InjectPresenter
    lateinit var periodicTransactionsPresenter: PeriodicTransactionsPresenter

    @ProvidePresenter
    fun providePresenter(): PeriodicTransactionsPresenter = periodicTransactionsPresenter

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

    override fun showTransactions(periodicTransactions: List<Transaction>) {
        adapter.updateDataSet(periodicTransactions)
    }


    private fun initUI() {
        adapter = PeriodicTransactionsRvAdapter(object : PeriodicTransactionsRvAdapter.OnLongClickCallback {
            override fun onLongClicked(item: Transaction) {
                showDialogFrag(item)
            }
        })
        rv_pendings.adapter = adapter
        rv_pendings.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        val linearLayoutManager = LinearLayoutManager(context)
        rv_pendings.layoutManager = linearLayoutManager
    }

    override fun onDeleteTransactionClicked(transaction: Transaction) {
        periodicTransactionsPresenter.deleteTransaction(transaction)
    }

    private fun showDialogFrag(transaction: Transaction) {
        val optionsDialog = OptionsDialogFragment.newInstance(transaction)
        optionsDialog.show(childFragmentManager, TAG)
    }

}