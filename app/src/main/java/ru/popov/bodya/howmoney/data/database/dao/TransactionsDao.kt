package ru.popov.bodya.howmoney.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import ru.popov.bodya.howmoney.domain.wallet.models.Transaction

@Dao
interface TransactionsDao : BaseDao<Transaction> {
    @Query("SELECT * FROM transactions WHERE periodic=0 AND template=0")
    fun getAllTransactions(): Flowable<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE periodic=1 AND template=0")
    fun getAllPeriodicTransactions(): Flowable<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE template=1")
    fun getAllTemplates(): Flowable<List<Transaction>>

    @Query("DELETE FROM transactions")
    fun deleteAllTransactions()

    @Query("SELECT * FROM transactions WHERE amount>0 AND periodic=0 AND template=0")
    fun getAllIncomeTransactions(): Flowable<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE walletId=:walletId AND amount>0 AND periodic=0 AND template=0")
    fun getAllIncomeTransactionsSumByWalletId(walletId: Int): Flowable<Double>

    @Query("SELECT SUM(amount) FROM transactions WHERE walletId=:walletId AND amount<0 AND periodic=0 AND template=0")
    fun getAllExpenseTransactionsSumByWalletId(walletId: Int): Flowable<Double>

    @Query("SELECT * FROM transactions WHERE amount<0 AND periodic=0 AND template=0")
    fun getAllExpenseTransactions(): Flowable<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE walletId=:walletId AND periodic=0 AND template=0")
    fun getAllTransactionsByWalletId(walletId: Int): Flowable<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE walletId=:walletId AND amount>0 AND periodic=0 AND template=0")
    fun getAllIncomeTransactionsByWalletId(walletId: Int): Flowable<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE walletId=:walletId AND amount<0 AND periodic=0 AND template=0")
    fun getAllExpenseTransactionsByWalletId(walletId: Int): Flowable<List<Transaction>>

    @Query("DELETE FROM transactions WHERE walletId=:walletId AND template=0")
    fun deleteAllTransactionsByWalletId(walletId: Int)

    @Query("DELETE FROM transactions WHERE id=:transactionId AND template=0")
    fun deleteTransactionById(transactionId: Int)

    @Query("SELECT * FROM transactions WHERE id=:transactionId AND template=0")
    fun getTransactionById(transactionId: Int): Flowable<Transaction>
}