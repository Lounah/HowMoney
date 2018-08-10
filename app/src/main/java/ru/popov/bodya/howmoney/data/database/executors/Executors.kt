package ru.popov.bodya.howmoney.data.database.executors

import java.util.concurrent.Executors

object Executors {
    private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

    fun ioThread(f: () -> Unit) {
        IO_EXECUTOR.execute(f)
    }
}