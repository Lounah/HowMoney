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

        itemView.tv_wallet_type.text = amount[position].name

        val drawableRight = when (amount[position].type) {
            Type.CASH -> itemView.resources.getDrawable(R.drawable.ic_cash_white)
            Type.BANK_ACCOUNT -> itemView.resources.getDrawable(R.drawable.ic_bank_white)
            Type.DEBIT_CARD -> itemView.resources.getDrawable(R.drawable.ic_credit_card_white)
        }

        itemView.iv_wallet_type.setImageDrawable(drawableRight)

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