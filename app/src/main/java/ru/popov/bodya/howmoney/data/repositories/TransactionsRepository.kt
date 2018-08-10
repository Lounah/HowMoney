package ru.popov.bodya.howmoney.data.repositories

import io.reactivex.Completable
import ru.popov.bodya.howmoney.data.database.dao.TransactionsDao
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction

class TransactionsRepository(private val transactionsDao: TransactionsDao) {
    fun addTransaction(transaction: Transaction) = Completable.fromAction { transactionsDao.insert(transaction) }

    fun addPeriodicTransaction(transaction: Transaction) = Completable.fromAction { transactionsDao.insert(transaction) }

    fun getAllTemplates() = transactionsDao.getAllTemplates()

    fun deleteAllTransactions() = Completable.fromAction { transactionsDao.deleteAllTransactions() }

    fun deleteAllTransactionsByWalletId(walletId: Int) = Completable.fromAction { transactionsDao.deleteAllTransactionsByWalletId(walletId) }

    fun deleteTransactionById(transactionId: Int) = Completable.fromAction { transactionsDao.deleteTransactionById(transactionId) }.subscribe()

    fun getTransactionById(transactionId: Int) = Completable.fromAction { transactionsDao.getTransactionById(transactionId) }.subscribe()

    fun getAllTransactions() = transactionsDao.getAllTransactions()

    fun getAllPeriodicTransactions() = transactionsDao.getAllPeriodicTransactions()

    fun getAllIncomeTransactions() = transactionsDao.getAllIncomeTransactions()

    fun getAllIncomeTransactionsSumByWalletId(walletId: Int) = transactionsDao.getAllIncomeTransactionsSumByWalletId(walletId)

    fun getAllExpenseTransactionsSumByWalletId(walletId: Int) = transactionsDao.getAllExpenseTransactionsSumByWalletId(walletId)

    fun getAllExpenseTransactions() = transactionsDao.getAllExpenseTransactions()

    fun getAllTransactionsByWallet(walletId: Int) = transactionsDao.getAllTransactionsByWalletId(walletId)

    fun getAllIncomeTransactionsByWallet(walletId: Int) = transactionsDao.getAllIncomeTransactionsByWalletId(walletId)

    fun getAllExpenseTransactionsByWallet(walletId: Int) = transactionsDao.getAllExpenseTransactionsByWalletId(walletId)

    fun deleteTransaction(transaction: Transaction) = Completable.fromAction { transactionsDao.delete(transaction) }

    fun updateTransaction(transaction: Transaction) = Completable.fromAction { transactionsDao.update(transaction) }
}