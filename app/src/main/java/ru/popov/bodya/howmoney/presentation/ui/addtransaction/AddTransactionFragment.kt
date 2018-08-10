package ru.popov.bodya.howmoney.presentation.ui.addtransaction

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.presentation.mvp.addtransaction.AddTransactionPresenter
import ru.popov.bodya.howmoney.presentation.mvp.addtransaction.AddTransactionView
import ru.popov.bodya.howmoney.presentation.ui.account.activities.AccountActivity
import ru.popov.bodya.howmoney.presentation.ui.addtransaction.adapters.CategoriesRVAdapter
import ru.popov.bodya.howmoney.presentation.ui.addtransaction.adapters.WalletsRvAdapter
import ru.popov.bodya.howmoney.presentation.ui.common.BaseFragment
import java.util.Date
import javax.inject.Inject

class AddTransactionFragment : BaseFragment(), AddTransactionView {
    override val TAG: String
        get() = "ADD_TRANSACTION_FRAGMENT"
    override val layoutRes: Int
        get() = R.layout.fragment_add_transaction
    override val toolbarTitleId: Int
        get() = R.string.create_transaction

    companion object {
        private const val MS_IN_DAY: Long = 86400000
    }

    private lateinit var categoriesAdapter: CategoriesRVAdapter
    private lateinit var walletsAdapter: WalletsRvAdapter

    private var selectedPeriodId = -1
    private lateinit var selectedWallet: Wallet
    private var selectedCurrency = Currency.RUB
    private var selectedCategory = Category.OTHER
    private lateinit var comment: String
    private var isPeriodic = false
    private var period: Long = 0
    private val date = Date()
    private var amount: Double = 0.0
    private var isIncome = false
    private var isTemplate = false

    @Inject
    @InjectPresenter
    lateinit var addTransactionPresenter: AddTransactionPresenter

    @ProvidePresenter
    fun provideAddTransactionPresenterPresenter(): AddTransactionPresenter = addTransactionPresenter

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

    override fun onTransactionCreated() {
        addTransactionPresenter.closeFragment(resources.getString(R.string.transaction_created))
    }

    override fun showWallets(wallets: List<Wallet>) {
        walletsAdapter.updateDataSet(wallets)
    }

    private fun initUI() {
        initBaseViews()
        initCategoriesList()
        initWalletsList()
        initCreateTransactionButton()
        initRadioGroups()
    }

    private fun initBaseViews() {
        if (isTemplate) btn_create_as_transaction.text = resources.getString(R.string.create_draft)
        rg_income_expense.check(R.id.btn_expense)
    }

    private fun initWalletsList() {
        walletsAdapter = WalletsRvAdapter(object : OnWalletSelectedCallback {
            override fun onWalletSelected(wallet: Wallet) {
                selectedWallet = wallet
            }
        })
        rv_wallets.adapter = walletsAdapter
    }

    private fun initCategoriesList() {
        categoriesAdapter = CategoriesRVAdapter(object : OnCategorySelectedCallback {
            override fun onCategorySelected(type: Category) {
                selectedCategory = type
            }
        })
        rv_categories.adapter = categoriesAdapter
    }

    private fun initCreateTransactionButton() {
        btn_create_as_transaction.setOnClickListener {
            comment = et_comment_on_transaction.text.toString()
            if (!::selectedWallet.isInitialized
                    || et_transaction_sum.text == null || et_transaction_sum.text.toString().isEmpty()) {
                Toast.makeText(context!!, R.string.create_transaction_error, Toast.LENGTH_SHORT).show()
            } else {
                amount = et_transaction_sum.text.toString().toDouble()
                isTemplate = switch_is_template.isChecked
                if (!isIncome)
                    amount = -amount
                val transaction = Transaction(date = date,
                        comment = comment, walletId = selectedWallet.id,
                        category = selectedCategory,
                        amount = amount, currency = selectedWallet.majorCurrency, periodic = isPeriodic,
                        template = isTemplate, period = period)
                addTransactionPresenter.createTransaction(transaction)
            }
        }
    }

    private fun initRadioGroups() {
        rg_income_expense.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_income -> isIncome = true
                R.id.btn_expense -> isIncome = false
            }
        }
        rg_period.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == selectedPeriodId) {
                rg_period.clearCheck()
                selectedPeriodId = checkedId
                isPeriodic = false
            } else {
                when (checkedId) {
                    R.id.btn_every_day -> period = MS_IN_DAY
                    R.id.btn_every_three_days -> period = MS_IN_DAY * 3
                    R.id.btn_every_week -> period = MS_IN_DAY * 7
                    R.id.btn_every_month -> period = MS_IN_DAY * 30
                }
                isPeriodic = true
            }
        }
    }

    interface OnCategorySelectedCallback {
        fun onCategorySelected(type: Category)
    }

    interface OnWalletSelectedCallback {
        fun onWalletSelected(wallet: Wallet)
    }
}