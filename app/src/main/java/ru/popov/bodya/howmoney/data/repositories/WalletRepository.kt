package ru.popov.bodya.howmoney.data.repositories

import io.reactivex.Completable
import ru.popov.bodya.howmoney.data.database.dao.WalletDao
import ru.popov.bodya.howmoney.domain.wallet.models.Wallet


class WalletRepository(private val walletDao: WalletDao) {
    fun getWallets() = walletDao.getAllWallets()

    fun getWalletById(walletId: Int) = walletDao.getWalletById(walletId)

    fun addWallet(wallet: Wallet) = Completable.fromAction { walletDao.insert(wallet) }

    fun deleteWallet(walletId: Int) = Completable.fromAction { walletDao.deleteWalletById(walletId) }

    fun updateWalletBalance(walletId: Int, newBalance: Double) = Completable.fromAction { walletDao.updateWalletBalance(walletId, newBalance) }

    fun increaseWalletBalance(walletId: Int, inc: Double) = Completable.fromCallable { walletDao.increaseWalletBalance(walletId, inc) }

    fun clearData() = Completable.fromAction {
        walletDao.deleteAllWallets()
        walletDao.resetAllWallets()
    }

}