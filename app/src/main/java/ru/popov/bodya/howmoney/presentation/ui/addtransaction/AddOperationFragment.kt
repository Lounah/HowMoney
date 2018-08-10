package ru.popov.bodya.howmoney.presentation.ui.addtransaction


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import ru.popov.bodya.howmoney.presentation.ui.common.BaseFragment
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.fragment_new_transaction.*
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.presentation.ui.account.activities.AccountActivity
import ru.popov.bodya.howmoney.presentation.ui.addtransaction.addtemplate.AddTemplateFragment


class AddOperationFragment : BaseFragment() {

    private lateinit var adapter: TransactionTypePagerAdapter

    override val TAG: String
        get() = "NEW_TRANSACTION_FRAGMENT"
    override val layoutRes: Int
        get() = R.layout.fragment_new_transaction
    override val toolbarTitleId: Int
        get() = R.string.create_transaction

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun setUpToolbarTitle(resId: Int) {
        (activity as AccountActivity).updateToolBar(R.string.create_transaction)
    }

    private fun initUI() {
        adapter = TransactionTypePagerAdapter(childFragmentManager)
        vp_transaction_type_selector.adapter = adapter
        vp_transaction_type_selector.currentItem = 0
        tl_transaction_types.setupWithViewPager(vp_transaction_type_selector)
    }

    inner class TransactionTypePagerAdapter(fragmentManager: FragmentManager)
        : FragmentPagerAdapter(fragmentManager) {

        private val NUM_ITEMS = 2

        override fun getCount(): Int {
            return NUM_ITEMS
        }

        override fun getItem(position: Int): Fragment? =
                when (position) {
                    0 -> AddTransactionFragment()
                    1 -> AddTemplateFragment()
                    else -> null
                }

        override fun getPageTitle(position: Int): CharSequence? = when (position) {
            0 -> resources.getString(R.string.transaction)
            1 -> resources.getString(R.string.draft)
            else -> resources.getString(R.string.transaction)
        }
    }

}