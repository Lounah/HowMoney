package ru.popov.bodya.howmoney.presentation.ui.wallet

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction


class OptionsDialogFragment : DialogFragment() {
    private lateinit var clickListener: OnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clickListener = parentFragment as OnClickListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val transaction = arguments?.getParcelable<Transaction>(TRANSACTION_ID)

        return AlertDialog.Builder(context!!)
                .setItems(R.array.transaction_options, { dialog, which ->
                    when (which) {
                        0 -> clickListener.onDeleteTransactionClicked(transaction!!)
                    }
                })
                .create()
    }

    interface OnClickListener {
        fun onDeleteTransactionClicked(transaction: Transaction)
    }

    companion object {
        private val TRANSACTION_ID = "TRANSACTION_ID"
        fun newInstance(transaction: Transaction): OptionsDialogFragment {
            val frag = OptionsDialogFragment()
            val args = Bundle()
            args.putParcelable(TRANSACTION_ID, transaction)
            frag.arguments = args
            return frag
        }
    }

}