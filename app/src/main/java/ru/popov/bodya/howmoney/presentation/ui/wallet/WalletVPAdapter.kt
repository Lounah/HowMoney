package ru.popov.bodya.howmoney.presentation.ui.wallet

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_balance.view.*
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Type
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet

class WalletVPAdapter : PagerAdapter() {

    private var amount = mutableListOf<Wallet>()

    override fun getCount() = amount.size

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater = container.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView = mLayoutInflater
                .inflate(R.layout.item_balance, container, false)

        itemView.tv_wallet_type.text = when (amount[position].type) {
            Type.DEBIT_CARD -> itemView.resources.getString(R.string.credit_card)
            Type.BANK_ACCOUNT -> itemView.resources.getString(R.string.bank_account)
            Type.CASH -> itemView.resources.getString(R.string.wallet)
        }
        itemView.tv_balance.setSymbol(amount[position].majorCurrency.toString())
        itemView.tv_balance.amount = amount[position].amount.toFloat()

        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    // TODO: USE DIFF UTIL
    fun updateDataSet(amount: List<Wallet>?) {
        if (amount != null) {
            this.amount.clear()
            this.amount.addAll(amount)
            notifyDataSetChanged()
        }
    }

}