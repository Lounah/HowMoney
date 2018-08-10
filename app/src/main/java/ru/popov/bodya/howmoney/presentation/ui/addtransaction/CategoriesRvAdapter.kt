package ru.popov.bodya.howmoney.presentation.ui.addtransaction

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Category
import ru.popov.bodya.howmoney.presentation.util.ResourcesSelector


class CategoriesRVAdapter(private val callback: AddTransactionFragment.OnCategorySelectedCallback)
    : RecyclerView.Adapter<CategoriesRVAdapter.ViewHolder>() {

    private val categories = mutableListOf(Category.HOME, Category.AUTO,
            Category.EDUCATION, Category.TREATMENT, Category.REST,
            Category.CLOTHES, Category.COMMUNAL_PAYMENTS, Category.FOOD,
            Category.SALARY, Category.FAMILY, Category.OTHER)

    private var selectedIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction_category,
                parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val categoryName: TextView by lazy {
            itemView.findViewById<TextView>(R.id.tv_category)
        }

        private val categoryIcon: ImageView by lazy {
            itemView.findViewById<ImageView>(R.id.iv_category)
        }

        fun bind(item: Category, position: Int) = with(itemView) {

            val iconImageResource = ResourcesSelector.fromTransactionTypeToDrawable(item)
            categoryName.text = ResourcesSelector.fromTransactionTypeToString(item, itemView)
            categoryIcon.setImageResource(iconImageResource)

            if (position == selectedIndex)
                itemView.setBackgroundColor(resources.getColor(R.color.greyWarm)) else
                itemView.setBackgroundColor(resources.getColor(android.R.color.transparent))

            itemView.setOnClickListener {
                selectedIndex = position
                callback.onCategorySelected(categories[adapterPosition])
                notifyDataSetChanged()
            }
        }
    }
}