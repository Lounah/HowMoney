package ru.popov.bodya.howmoney.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Single
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet

@Dao
interface WalletDao : BaseDao<Wallet> {
    @Query("SELECT * FROM wallets")
    fun getAllWallets(): Single<List<Wallet>>

    @Query("SELECT * FROM wallets WHERE id=:id")
    fun getWalletById(id: Int): Single<Wallet>

    @Query("UPDATE wallets SET amount=:newBalance WHERE id=:walletId")
    fun updateWalletBalance(walletId: Int, newBalance: Double)

    @Query("UPDATE wallets SET amount=amount+:inc WHERE id=:walletId")
    fun increaseWalletBalance(walletId: Int, inc: Double)
}