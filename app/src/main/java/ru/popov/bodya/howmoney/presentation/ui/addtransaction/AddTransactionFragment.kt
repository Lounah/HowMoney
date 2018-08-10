package ru.popov.bodya.howmoney.presentation.ui.addtransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import ru.popov.bodya.core.mvp.AppFragment
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.presentation.mvp.addtransaction.AddTransactionPresenter
import ru.popov.bodya.howmoney.presentation.mvp.addtransaction.AddTransactionView
import ru.popov.bodya.howmoney.presentation.ui.account.activities.AccountActivity
import ru.popov.bodya.howmoney.presentation.ui.common.BaseFragment
import java.util.*
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
        const val INCOME_KEY = "IS_INCOME"
        fun newInstance(isIncome: Boolean) = AddTransactionFragment().apply {
            val args = Bundle()
            args.putBoolean(INCOME_KEY, isIncome)
            arguments = args
        }
    }

    private lateinit var categoriesAdapter: CategoriesRVAdapter
    private lateinit var walletsAdapter: WalletsRvAdapter
    private lateinit var inputDisposable: Disposable

    private lateinit var selectedWallet: Wallet
    private lateinit var selectedCurrency: Currency
    private lateinit var selectedCategory: Category
    private lateinit var comment: String
    private var isPeriodic = false
    private var period: Long = 0
    private val date = Date()
    private var amount: Double = 0.0
    private var isIncome = false

    @Inject
    @InjectPresenter
    lateinit var addTransactionPresenter: AddTransactionPresenter

    @ProvidePresenter
    fun provideAddTransactionPresenterPresenter(): AddTransactionPresenter = addTransactionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        isIncome = arguments?.getBoolean(INCOME_KEY)!!
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
        initPeriodSeekBar()
        initCategoriesList()
        initWalletsList()
        initInputListeners()
        initCreateTransactionButton()
        initRadioGroups()
    }

    private fun initPeriodSeekBar() {
        sb_period.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                isPeriodic = progress != 0

                tv_period.text = when (progress) {
                    0 -> resources.getString(R.string.unspecified)
                    1 -> resources.getString(R.string.every_day)
                    2 -> resources.getString(R.string.every_three_days)
                    3 -> resources.getString(R.string.every_week)
                    4 -> resources.getString(R.string.every_month)
                    else -> resources.getString(R.string.unspecified)
                }

                period = when (progress) {
                    0 -> 0
                    1 -> MS_IN_DAY
                    2 -> MS_IN_DAY * 3L
                    3 -> MS_IN_DAY * 7L
                    4 -> MS_IN_DAY * 30L
                    else -> 0
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
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

    private fun initInputListeners() {
        val inputObserver: Observable<Boolean> = Observable.combineLatest(
                RxTextView.textChanges(et_transaction_sum),
                RxTextView.textChanges(et_comment_on_transaction),
                BiFunction { amount, comment ->
                    amount.isNotEmpty()
                            && comment.isNotEmpty()
                            && rg_currencies.checkedRadioButtonId != -1
                            && ::selectedWallet.isInitialized
                            && ::selectedCategory.isInitialized
                })
        inputDisposable = inputObserver.subscribe(btn_create_transaction::setEnabled)
    }

    private fun initCreateTransactionButton() {
        btn_create_transaction.setOnClickListener {
            amount = et_transaction_sum.text.toString().toDouble()
            if (!isIncome)
                amount = -amount
            comment = et_comment_on_transaction.text.toString()
            val transaction = Transaction(date = date,
                    comment = comment, walletId = selectedWallet.id,
                    amount = amount, currency = selectedCurrency, periodic = isPeriodic, period = period)
            addTransactionPresenter.createTransaction(transaction)
        }
    }

    private fun initRadioGroups() {
        rg_currencies.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_rub -> selectedCurrency = Currency.RUB
                R.id.btn_usd -> selectedCurrency = Currency.USD
                R.id.btn_eur -> selectedCurrency = Currency.EUR
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