package ru.popov.bodya.howmoney.presentation.util

import android.view.View
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Category
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.popov.bodya.howmoney.domain.wallet.models.Type

class ResourcesSelector private constructor() {

    companion object {
        fun fromTransactionTypeToString(type: Category, view: View) = when (type) {
            Category.AUTO -> view.resources.getString(R.string.auto)
            Category.TREATMENT -> view.resources.getString(R.string.treatment)
            Category.REST -> view.resources.getString(R.string.rest)
            Category.FAMILY -> view.resources.getString(R.string.family)
            Category.CLOTHES -> view.resources.getString(R.string.clothes)
            Category.EDUCATION -> view.resources.getString(R.string.education)
            Category.COMMUNAL_PAYMENTS -> view.resources.getString(R.string.communal_payments)
            Category.HOME -> view.resources.getString(R.string.home)
            Category.FOOD -> view.resources.getString(R.string.food)
            Category.OTHER -> view.resources.getString(R.string.other)
            Category.SALARY -> view.resources.getString(R.string.salary)
        }!!

        fun fromTransactionTypeToDrawable(type: Category) = when (type) {
            Category.AUTO -> R.drawable.ic_auto
            Category.TREATMENT -> R.drawable.ic_treatment
            Category.REST -> R.drawable.ic_rest
            Category.FAMILY -> R.drawable.ic_family
            Category.CLOTHES -> R.drawable.ic_clothes
            Category.EDUCATION -> R.drawable.ic_education
            Category.COMMUNAL_PAYMENTS -> R.drawable.ic_communal_payments
            Category.HOME -> R.drawable.ic_home
            Category.FOOD -> R.drawable.ic_food
            Category.OTHER -> R.drawable.ic_other
            Category.SALARY -> R.drawable.ic_salary
        }

        fun fromWalletTypeToDrawable(type: Type) = when (type) {
            Type.CASH -> R.drawable.ic_cash
            Type.DEBIT_CARD -> R.drawable.ic_credit_card
            Type.BANK_ACCOUNT -> R.drawable.ic_bank
        }

        fun fromWalletTypeToString(type: Type, view: View) = when (type) {
            Type.CASH -> view.resources.getString(R.string.cash)
            Type.DEBIT_CARD -> view.resources.getString(R.string.credit_card)
            Type.BANK_ACCOUNT -> view.resources.getString(R.string.bank_account)
        }
    }
}