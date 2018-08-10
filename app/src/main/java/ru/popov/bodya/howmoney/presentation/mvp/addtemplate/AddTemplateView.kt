package ru.popov.bodya.howmoney.presentation.mvp.addtemplate

import ru.popov.bodya.core.mvp.AppView
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction

interface AddTemplateView : AppView {
    fun onTemplateCreated()
    fun showTemplates(templates: List<Transaction>)
    fun createFromTemplate(template: Transaction)
}