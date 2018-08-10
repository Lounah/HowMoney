package ru.popov.bodya.howmoney.presentation.mvp.addtemplate

import com.arellomobile.mvp.InjectViewState
import ru.popov.bodya.core.extensions.connect
import ru.popov.bodya.core.mvp.AppPresenter
import ru.popov.bodya.core.rx.RxSchedulersTransformer
import ru.popov.bodya.howmoney.domain.wallet.WalletInteractor
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class AddTemplatePresenter @Inject constructor(
        private val walletInteractor: WalletInteractor,
        private val rxSchedulersTransformer: RxSchedulersTransformer,
        private val router: Router
) : AppPresenter<AddTemplateView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        walletInteractor.getAllTemplates()
                .compose(rxSchedulersTransformer.ioToMainTransformerFlowable())
                .subscribe(viewState::showTemplates)
                .connect(compositeDisposable)
    }

    fun createTemplate(template: Transaction) {
        walletInteractor.createTransaction(template.copy(id = 0, template = false))
                .compose(rxSchedulersTransformer.ioToMainTransformerCompletable())
                .subscribe(viewState::onTemplateCreated)
                .connect(compositeDisposable)
    }

    fun closeFragment(exitMsg: String) {
        router.exitWithMessage(exitMsg)
    }

}