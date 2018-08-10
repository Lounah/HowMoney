package ru.popov.bodya.howmoney.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.reactivex.Flowable
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet

@Dao
interface WalletDao : BaseDao<Wallet> {
    @Query("SELECT * FROM wallets")
    fun getAllWallets(): Flowable<List<Wallet>>

    @Query("SELECT * FROM wallets WHERE id=:id")
    fun getWalletById(id: Int): Flowable<Wallet>

    @Query("UPDATE wallets SET amount=:newBalance WHERE id=:walletId")
    fun updateWalletBalance(walletId: Int, newBalance: Double)

    @Query("UPDATE wallets SET amount=(amount+:inc) WHERE id=:walletId")
    fun increaseWalletBalance(walletId: Int, inc: Double)

    @Query("DELETE FROM wallets WHERE id=:walletId")
    fun deleteWalletById(walletId: Int)

    @Query("DELETE FROM wallets WHERE id!=1")
    fun deleteAllWallets()

    @Query("UPDATE wallets SET amount=0.0")
    fun resetAllWallets()
}