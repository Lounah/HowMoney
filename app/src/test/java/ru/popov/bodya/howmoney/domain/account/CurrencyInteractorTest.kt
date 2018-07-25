package ru.popov.bodya.howmoney.domain.account

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import ru.popov.bodya.howmoney.data.repositories.CurrencyRepository
import ru.popov.bodya.howmoney.domain.account.interactors.CurrencyInteractor
import ru.popov.bodya.howmoney.domain.account.models.Currency
import ru.popov.bodya.howmoney.domain.operation.models.Operation
import ru.popov.bodya.howmoney.domain.operation.models.OperationType.ADDITION
import ru.popov.bodya.howmoney.domain.operation.models.OperationType.WITHDRAWAL

/**
 * @author popovbodya
 */
class CurrencyInteractorTest {

    private lateinit var currencyInteractor: CurrencyInteractor
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setUp() {
        currencyRepository  = mock(CurrencyRepository::class.java)
        currencyInteractor = CurrencyInteractor(currencyRepository)
    }

    @Test
    fun testCalculateBalance() {
        val operationList = createStubOperationList()
        val actial: Long = currencyInteractor.calculateBalance(operationList)
        assertThat(actial, `is`(-212L))

    }

    private fun createStubOperationList() = listOf<Operation>(
            Operation(WITHDRAWAL, Currency.RUB, 50),
            Operation(ADDITION, Currency.USD, 3),
            Operation(ADDITION, Currency.RUB, 15),
            Operation(ADDITION, Currency.RUB, 3),
            Operation(WITHDRAWAL, Currency.USD, 6)

    )
}