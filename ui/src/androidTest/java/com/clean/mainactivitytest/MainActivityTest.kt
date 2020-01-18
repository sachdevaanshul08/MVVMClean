package com.clean.mainactivitytest

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.clean.BaseTest
import com.clean.R
import com.clean.activity.MainActivity
import com.clean.utils.APICallsSuccessDispatcher
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseTest() {

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java, false, false)

    override fun isMockServerEnabled(): Boolean {
        return true
    }

    @Test
    fun CheckIfProgressBarShowingOnLaunch() {
        mockServer.setDispatcher(APICallsSuccessDispatcher())
        activityTestRule.launchActivity(Intent())
        Espresso.onView(withId(R.id.frame_progressbar)).check(matches(not(isDisplayed())));
    }





}