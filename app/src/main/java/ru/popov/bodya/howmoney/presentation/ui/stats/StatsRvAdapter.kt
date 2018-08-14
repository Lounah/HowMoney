package ru.popov.bodya.howmoney.presentation.ui.stats

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.fabiomsr.moneytextview.MoneyTextView
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Stats
import ru.popov.bodya.howmoney.presentation.util.ResourcesSelector

class StatsRvAdapter : RecyclerView.Adapter<StatsRvAdapter.ViewHolder>() {

    private val stats = mutableListOf<Stats>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_statistics, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = stats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stat = stats[position]
        holder.bind(stat)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val icon: ImageView by lazy {
            itemView.findViewById<ImageView>(R.id.iv_category)
        }

        private val transactionType: TextView by lazy {
            itemView.findViewById<TextView>(R.id.tv_category)
        }

        private val incomesSum: MoneyTextView by lazy {
            itemView.findViewById<MoneyTextView>(R.id.tv_incomes)
        }

        private val expenseSum: MoneyTextView by lazy {
            itemView.findViewById<MoneyTextView>(R.id.tv_expenses)
        }

        fun bind(item: Stats) = with(itemView) {

            transactionType.text = ResourcesSelector.fromTransactionTypeToString(item.category, itemView)

            val iconImageResource = ResourcesSelector.fromTransactionTypeToDrawable(item.category)

            icon.setImageResource(iconImageResource)

            incomesSum.amount = item.incomes.toFloat()

            expenseSum.amount = item.expenses.toFloat()
        }
    }

    // TODO: USE DIFF UTIL
    fun updateDataSet(stats: List<Stats>?) {
        if (stats != null) {
            this.stats.clear()
            this.stats.addAll(stats)
            notifyDataSetChanged()
        }
    }
}