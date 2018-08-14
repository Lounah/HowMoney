package ru.popov.bodya.howmoney.domain.wallet

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toFlowable
import ru.popov.bodya.howmoney.data.repositories.CurrencyRateRepository
import ru.popov.bodya.howmoney.data.repositories.TransactionsRepository
import ru.popov.bodya.howmoney.data.repositories.WalletRepository
import ru.popov.bodya.howmoney.domain.wallet.models.*
import ru.popov.bodya.howmoney.domain.wallet.models.Currency
import java.util.*

class WalletInteractor(private val currencyRateRepository: CurrencyRateRepository,
                       private val walletRepository: WalletRepository,
                       private val transactionsRepository: TransactionsRepository) {

    fun createWallet(wallet: Wallet): Completable = walletRepository.addWallet(wallet)

    fun deleteWallet(walletId: Int) = walletRepository.deleteWallet(walletId)
            .andThen(transactionsRepository.deleteAllTransactionsByWalletId(walletId))

    fun deleteTransaction(transaction: Transaction): Completable {
        return transactionsRepository.deleteTransaction(transaction).andThen {
            if (!transaction.periodic)
                walletRepository.increaseWalletBalance(transaction.walletId, -transaction.amount).subscribe()
        }
    }

    fun getAllPeriodicTransactions() = transactionsRepository.getAllPeriodicTransactions()

    fun addTransaction(transaction: Transaction) = transactionsRepository.addTransaction(transaction)

    fun getWalletBalance(walletId: Int): Flowable<Double> = walletRepository.getWalletById(walletId).map { it.amount }

    fun getMajorCurrencyForWallet(walletId: Int): Flowable<Currency> = walletRepository.getWalletById(walletId).map { it.majorCurrency }

    fun getAllWallets(): Flowable<List<Wallet>> = walletRepository.getWallets()

    fun getIncomeSumFromAllWalletsByWalletId(walletId: Int): Flowable<Double> = transactionsRepository.getAllIncomeTransactionsSumByWalletId(walletId)

    fun getExpenseSumFromAllWalletsByWalletId(walletId: Int): Flowable<Double> = transactionsRepository.getAllExpenseTransactionsSumByWalletId(walletId)

    fun deleteAllTransactions() =
            transactionsRepository.deleteAllTransactions()
                    .andThen(walletRepository.clearData())

    /*
        Лишний сабскрайб, который я не знаю как задиспоузить
     */
    fun getAllTransactions(): Flowable<List<Transaction>> {
        checkForPeriodicTransactionsAndExecuteIfShould().subscribe()
        return transactionsRepository.getAllTransactions()
    }

    fun getAllTemplates(): Flowable<List<Transaction>> = transactionsRepository.getAllTemplates()

    fun getAllIncomeTransactions(): Flowable<List<Transaction>> = transactionsRepository.getAllIncomeTransactions()

    fun getAllExpenseTransactions(): Flowable<List<Transaction>> = transactionsRepository.getAllExpenseTransactions()

    fun getAllIncomeTransactionsByWallet(walletId: Int): Flowable<List<Transaction>> = transactionsRepository.getAllIncomeTransactionsByWallet(walletId)

    fun getAllExpenseTransactionsByWallet(walletId: Int): Flowable<List<Transaction>> = transactionsRepository.getAllExpenseTransactionsByWallet(walletId)

    fun getAllTransactionsByWallet(walletId: Int): Flowable<List<Transaction>> = transactionsRepository.getAllTransactionsByWallet(walletId)

    fun getExchangeRate(fromCurrency: Currency, toCurrency: Currency) = currencyRateRepository.getExchangeRate(fromCurrency, toCurrency)

    /*
        пока что приложение не поддерживает добавление транзакций в валюте отличной от основной на кошельке
     */
    fun createTransaction(transaction: Transaction): Completable {
        return if (transaction.periodic)
            transactionsRepository.addPeriodicTransaction(transaction).andThen { putTransactionOnWalletWithTheSameCurrency(transaction.copy(id = 0, periodic = false)).subscribe() }
        else putTransactionOnWalletWithTheSameCurrency(transaction)
    }

    fun getStatsByWalletId(walletId: Int): Flowable<List<Stats>> {
        return transactionsRepository.getAllTransactionsByWallet(walletId)
                .map { it -> mapTransactionsToStats(it) }
    }

    private fun putTransactionOnWalletWithTheSameCurrency(transaction: Transaction): Completable {
        return walletRepository.increaseWalletBalance(transaction.walletId, transaction.amount).doOnComplete {
            transactionsRepository.addTransaction(transaction).subscribe()
        }
    }

    private fun putTransactionOnWalletWithDifferentCurrency(transaction: Transaction): Flowable<Double> {
        return walletRepository.getWalletById(transaction.walletId)
                .flatMap { wallet -> currencyRateRepository.getExchangeRate(transaction.currency, wallet.majorCurrency).toFlowable() }
                .map { rate -> rate * transaction.amount }
                .doOnNext { inc ->
                    walletRepository.increaseWalletBalance(transaction.walletId, inc)
                            .andThen { transactionsRepository.addTransaction(transaction) }.subscribe()
                }

    }


    /*
        Здесь все очень печально -- в плане самого алгоритма вроде норм
        но вот rx -- не умею его использовать, поэтому пришлось нагородить такое вот
     */

    private fun checkForPeriodicTransactionsAndExecuteIfShould(): Flowable<Unit> {
        return transactionsRepository.getAllPeriodicTransactions().map {
            val now = Date()
            val random = Random()
            it.filter { it.date.time <= now.time }.forEach {
                val numOfTransactionsWhichShouldBeExecuted = (now.time - it.date.time) / it.period!!
                for (i in 0 until numOfTransactionsWhichShouldBeExecuted) {
                    val newDate = it.date.time + (i * it.period)
                    val scheduledTransaction = it.copy(id = random.nextInt(), periodic = false, date = Date(newDate))
                    createTransaction(scheduledTransaction).subscribe()
                }
                val updatedPeriodicTransaction = it.copy(date = Date(now.time + it.period))
                transactionsRepository.updateTransaction(updatedPeriodicTransaction).subscribe()
            }
        }
    }

    private fun mapTransactionsToStats(transactions: List<Transaction>): List<Stats> {
        val stats = mutableListOf<Stats>()
        transactions.groupBy { it.category }
                .forEach { category, list ->
                    stats.add(Stats(category,
                            list.filter { it.amount > 0 }.sumByDouble { it.amount },
                            list.filter { it.amount < 0 }.sumByDouble { it.amount }))
                }
        return stats
    }
}