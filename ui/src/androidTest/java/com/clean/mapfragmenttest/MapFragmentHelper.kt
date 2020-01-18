package com.clean.mapfragmenttest

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.clean.R

class MapFragmentHelper {

    fun waitForCondition(idlingResource: IdlingResource?) = apply {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    fun assertMapVisible() = apply {
        onView(firstTypeMatcher).check(matches(isDisplayed()))
    }


    companion object {
        private val toolbarMatcher = withId(R.id.toolbar)
        private val errorViewMatcher = withId(R.id.retry_button)
        private val firstTypeMatcher = withId(R.id.frame_map)
    }
}