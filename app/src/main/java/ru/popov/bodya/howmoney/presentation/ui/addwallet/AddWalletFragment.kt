package ru.popov.bodya.howmoney.presentation.ui.addwallet

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_add_wallet.*
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import ru.popov.bodya.howmoney.domain.wallet.models.Type
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet

class AddWalletFragment : BottomSheetDialogFragment() {

    private var walletName: String = ""
    private lateinit var walletType: Type
    private lateinit var majorCurrency: Currency

    private lateinit var callback: AddWalletFragment.OnWalletCreatedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = activity as AddWalletFragment.OnWalletCreatedCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initRadioGroups()
        initCreateButton()
    }

    private fun initCreateButton() {
        rg_currencies.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_rub -> majorCurrency = Currency.RUB
                R.id.btn_usd -> majorCurrency = Currency.USD
                R.id.btn_eur -> majorCurrency = Currency.EUR
            }
        }
        rg_wallet_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_debit_card -> walletType = Type.DEBIT_CARD
                R.id.btn_cash -> walletType = Type.CASH
                R.id.btn_bank_account -> walletType = Type.BANK_ACCOUNT
            }
        }
        btn_add_wallet.setOnClickListener { composeWallet() }
    }

    private fun initRadioGroups() {
        rg_currencies.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_rub -> majorCurrency = Currency.RUB
                R.id.btn_usd -> majorCurrency = Currency.USD
                R.id.btn_eur -> majorCurrency = Currency.EUR
            }
        }

        rg_wallet_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_bank_account -> walletType = Type.BANK_ACCOUNT
                R.id.btn_cash -> walletType = Type.CASH
                R.id.btn_debit_card -> walletType = Type.DEBIT_CARD
            }
        }
    }

    private fun composeWallet() {
        if (et_wallet_name.text.isEmpty() || rg_wallet_type.checkedRadioButtonId == -1 || rg_currencies.checkedRadioButtonId == -1) {
            Toast.makeText(context!!, R.string.please_fill_all_fields, Toast.LENGTH_SHORT).show()
        } else {
            walletName = et_wallet_name.text.toString()
            callback.onWalletComposed(Wallet(0, 0.0, walletType, majorCurrency, walletName))
        }
    }

    interface OnWalletCreatedCallback {
        fun onWalletComposed(wallet: Wallet)
    }
}