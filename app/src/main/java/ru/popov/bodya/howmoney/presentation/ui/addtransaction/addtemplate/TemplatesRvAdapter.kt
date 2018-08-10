package ru.popov.bodya.howmoney.presentation.ui.addtransaction.addtemplate

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.fabiomsr.moneytextview.MoneyTextView
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.popov.bodya.howmoney.presentation.util.ResourcesSelector

class TemplatesRvAdapter(private val callback: AddTemplateFragment.OnTemplateSelectedCallback) : RecyclerView.Adapter<TemplatesRvAdapter.ViewHolder>() {

    private val templates = mutableListOf<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_template, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = templates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = templates[position]
        holder.bind(action)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val icon: ImageView by lazy {
            itemView.findViewById<ImageView>(R.id.iv_category)
        }

        private val transactionType: TextView by lazy {
            itemView.findViewById<TextView>(R.id.tv_transaction_category)
        }

        private val description: TextView by lazy {
            itemView.findViewById<TextView>(R.id.tv_template_name)
        }

        private val amount: MoneyTextView by lazy {
            itemView.findViewById<MoneyTextView>(R.id.tv_amount)
        }

        fun bind(item: Transaction) = with(itemView) {

            description.text = item.comment

            transactionType.text = ResourcesSelector.fromTransactionTypeToString(item.category, itemView)

            val iconImageResource = ResourcesSelector.fromTransactionTypeToDrawable(item.category)

            icon.setImageResource(iconImageResource)

            if (item.amount < 0) {
                amount.setBaseColor(itemView.resources.getColor(R.color.colorExpense))
                amount.setDecimalsColor(itemView.resources.getColor(R.color.colorExpense))
            } else {
                amount.setBaseColor(itemView.resources.getColor(R.color.colorIncome))
                amount.setDecimalsColor(itemView.resources.getColor(R.color.colorIncome))
            }
            amount.amount = item.amount.toFloat()
            amount.setSymbol(item.currency.toString())

            itemView.setOnClickListener { callback.onTemplateSelected(templates[adapterPosition]) }
        }
    }

    // TODO: USE DIFF UTIL
    fun updateDataSet(transactions: List<Transaction>?) {
        if (transactions != null) {
            this.templates.clear()
            this.templates.addAll(transactions)
            notifyDataSetChanged()
        }
    }
}