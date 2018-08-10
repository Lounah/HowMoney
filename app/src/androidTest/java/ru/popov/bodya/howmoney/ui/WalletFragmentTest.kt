package ru.popov.bodya.howmoney.ui


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.popov.bodya.howmoney.R
import ru.popov.bodya.howmoney.presentation.ui.account.activities.AccountActivity
import ru.popov.bodya.howmoney.presentation.ui.addtransaction.AddOperationFragment
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import org.hamcrest.Matchers.`is`


@RunWith(AndroidJUnit4::class)
class WalletFragmentTest {

    @Rule
    @JvmField
    val activityTestRule: ActivityTestRule<AccountActivity> = ActivityTestRule(AccountActivity::class.java, true, true)

    @Test
    fun newIncome_createTransaction() {

        activityTestRule.activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, AddOperationFragment())
                .commit()

        onView(withId(R.id.btn_income)).perform(click())

        scrollToView(R.id.btn_create_as_transaction)

        onView(withId(R.id.btn_create_as_transaction)).perform(click())

        onView(withText(R.string.please_fill_all_fields))
                .inRoot(withDecorView(not(`is`(activityTestRule.activity.window.decorView))))
                .check(matches(isDisplayed()))

        scrollToView(R.id.et_transaction_sum)

        typeTextInEditText(R.id.et_transaction_sum, "100.0")

        onView(withId(R.id.btn_rub)).perform(click())

        performClickOnItemInRecyclerView(R.id.rv_categories, 0)

        scrollToView(R.id.btn_create_as_transaction)
        performClickOnItemInRecyclerView(R.id.rv_wallets, 0)

        typeTextInEditText(R.id.et_comment_on_transaction, "Test Comment")

        closeSoftKeyboard()

        onView(withId(R.id.btn_create_as_transaction)).perform(click())
    }

    @Test
    fun check_IfTransactionCreated() {
        onView(withId(R.id.rv_transactions))
                .check(matches(hasDescendant(withText("Test Comment"))))
    }

    private fun scrollToView(viewId: Int) {
        onView(withId(viewId))
                .perform(scrollTo())
    }

    private fun performClickOnItemInRecyclerView(viewId: Int, itemPosition: Int) {
        onView(withId(viewId))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemPosition, click()))
    }

    private fun typeTextInEditText(viewId: Int, text: String) {
        onView(withId(viewId)).perform(replaceText(text))
    }

    private fun checkButtonState(shouldBeEnabled: Boolean, viewId: Int) {
        if (shouldBeEnabled) onView(withId(viewId)).check(matches((isEnabled())))
        else onView(withId(viewId)).check(matches(not((isEnabled()))))
    }
}