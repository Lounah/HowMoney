package ru.popov.bodya.howmoney.presentation.ui.addtransaction

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet
import ru.popov.bodya.howmoney.presentation.util.ResourcesSelector

class WalletsRvAdapter(private val callback: AddTransactionFragment.OnWalletSelectedCallback)
    : RecyclerView.Adapter<WalletsRvAdapter.ViewHolder>() {

    private val wallets = mutableListOf<Wallet>()

    private var selectedIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wallet_type,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = wallets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wallet = wallets[position]
        holder.bind(wallet, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val walletName: TextView by lazy {
            itemView.findViewById<TextView>(R.id.tv_wallet_type)
        }

        private val walletIcon: ImageView by lazy {
            itemView.findViewById<ImageView>(R.id.iv_wallet_type)
        }

        fun bind(item: Wallet, position: Int) = with(itemView) {

            val iconImageResource = ResourcesSelector.fromWalletTypeToDrawable(item.type)
            walletName.text = ResourcesSelector.fromWalletTypeToString(item.type, itemView)
            walletIcon.setImageResource(iconImageResource)

            if (position == selectedIndex)
                itemView.setBackgroundColor(resources.getColor(R.color.greyWarm)) else
                itemView.setBackgroundColor(resources.getColor(android.R.color.transparent))

            itemView.setOnClickListener {
                selectedIndex = position
                callback.onWalletSelected(wallets[adapterPosition])
                notifyDataSetChanged()
            }
        }
    }

    fun updateDataSet(wallets: List<Wallet>?) {
        if (wallets != null) {
            this.wallets.clear()
            this.wallets.addAll(wallets)
            notifyDataSetChanged()
        }
    }
}