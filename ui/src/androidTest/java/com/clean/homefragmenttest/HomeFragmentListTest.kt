package com.clean.homefragmenttest

import android.view.View
import androidx.test.espresso.IdlingRegistry
import androidx.test.rule.ActivityTestRule
import com.clean.BaseTest
import com.clean.R
import com.clean.activity.MainActivity
import com.clean.utils.APICallsErrorDispatcher
import com.clean.utils.APICallsSuccessDispatcher
import com.clean.utils.ViewVisibilityIdlingResource
import org.junit.Rule
import org.junit.Test

class HomeFragmentListTest : BaseTest() {
    @JvmField
    @Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, false, false)

    private var progressBarGoneIdlingResource: ViewVisibilityIdlingResource? = null

    override fun isMockServerEnabled(): Boolean {
        return true
    }

    override fun tearDown() {
        super.tearDown()
        IdlingRegistry.getInstance().unregister(progressBarGoneIdlingResource)
    }

    @Test
    fun displayVehicle() {
        mockServer.setDispatcher(APICallsSuccessDispatcher())
        activityTestRule.launchActivity(null)
        progressBarGoneIdlingResource =
            ViewVisibilityIdlingResource(
                activityTestRule.activity.findViewById(R.id.frame_progressbar),
                View.GONE
            )

        HomeFragmentHelper()
            .waitForCondition(progressBarGoneIdlingResource)
            .assertDataDisplayed()
            .assertVehicleTypeAtPosition(0, "POOLING") //Since our test data ("Sample.json")contains only
            .assertVehicleTypeAtPosition(1, "TAXI")  //Three items
            .assertVehicleTypeAtPosition(2, "POOLING")
    }

    @Test
    fun clickRecyclerviewItem() {
        mockServer.setDispatcher(APICallsSuccessDispatcher())
        activityTestRule.launchActivity(null)
        progressBarGoneIdlingResource =
            ViewVisibilityIdlingResource(
                activityTestRule.activity.findViewById(R.id.frame_progressbar),
                View.GONE
            )

        HomeFragmentHelper()
            .waitForCondition(progressBarGoneIdlingResource)
            .assertDataDisplayed()
            .clickItem(0)
    }

    @Test
    fun displayError() {
        mockServer.setDispatcher(APICallsErrorDispatcher())
        activityTestRule.launchActivity(null)
        progressBarGoneIdlingResource =
            ViewVisibilityIdlingResource(
                activityTestRule.activity.findViewById(R.id.frame_progressbar),
                View.GONE
            )

        HomeFragmentHelper()
            .waitForCondition(progressBarGoneIdlingResource)
            .assertErrorDisplayed()
    }


}