package com.clean.homefragmenttest

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.clean.R
import com.clean.utils.RecyclerViewMatcher

class HomeFragmentHelper {

    fun assertDataDisplayed() = apply {
        onView(recyclerViewMatcher).check(matches(isDisplayed()))
    }

    fun assertErrorDisplayed() = apply {
        onView(errorViewMatcher).check(matches(isDisplayed()))
    }

    fun waitForCondition(idlingResource: IdlingResource?) = apply {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    fun assertVehicleTypeAtPosition(position: Int, name: String) = apply {
        val itemMatcher = RecyclerViewMatcher(recyclerViewId)
            .atPositionOnView(
                position,
                vehicleNameId
            )
        onView(itemMatcher).check(matches(withText(name)))
    }

    fun clickItem(position: Int) = apply {
        val itemMatcher = RecyclerViewMatcher(recyclerViewId).atPosition(position)
        onView(itemMatcher).perform(click())
    }

    companion object {
        private const val recyclerViewId = R.id.recycler_view
        private const val vehicleNameId = R.id.lite_listrow_text

        private val recyclerViewMatcher = withId(R.id.recycler_view)
        private val errorViewMatcher = withId(R.id.btn_retry)
    }
}