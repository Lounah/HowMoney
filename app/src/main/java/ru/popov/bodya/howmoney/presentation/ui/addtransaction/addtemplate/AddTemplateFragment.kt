package ru.popov.bodya.howmoney.presentation.ui.addtransaction.addtemplate

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_new_template.*
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.popov.bodya.howmoney.presentation.mvp.addtemplate.AddTemplatePresenter
import ru.popov.bodya.howmoney.presentation.mvp.addtemplate.AddTemplateView
import ru.popov.bodya.howmoney.presentation.ui.common.BaseFragment
import javax.inject.Inject

class AddTemplateFragment : BaseFragment(), AddTemplateView {

    override val TAG: String
        get() = "ADD_TEMPLATE_FRAGMENT"
    override val layoutRes: Int
        get() = R.layout.fragment_new_template
    override val toolbarTitleId: Int
        get() = 0

    @Inject
    @InjectPresenter
    lateinit var addTemplatePresenter: AddTemplatePresenter

    @ProvidePresenter
    fun providePresenter(): AddTemplatePresenter = addTemplatePresenter

    private lateinit var templatesAdapter: TemplatesRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onTemplateCreated() {
        addTemplatePresenter.closeFragment(resources.getString(R.string.transaction_created))
    }

    override fun showTemplates(templates: List<Transaction>) {
        templatesAdapter.updateDataSet(templates)
    }

    override fun createFromTemplate(template: Transaction) {
        addTemplatePresenter.createTemplate(template)
    }

    private fun initUI() {
        templatesAdapter = TemplatesRvAdapter(object : OnTemplateSelectedCallback {
            override fun onTemplateSelected(template: Transaction) {
                createFromTemplate(template)
            }

        })
        rv_templates.adapter = templatesAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        rv_templates.layoutManager = linearLayoutManager

    }

    override fun setUpToolbarTitle(resId: Int) {}

    interface OnTemplateSelectedCallback {
        fun onTemplateSelected(template: Transaction)
    }

}