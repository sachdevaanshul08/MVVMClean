package com.clean.utils

import android.view.View
import androidx.test.espresso.IdlingResource

/**

1) isIdleNow() implement the logic when the resource is idle

2) registerIdleTransitionCallback will be called by Espresso when
the resource is registered and will give you a ResourceCallback.

3) When the resource goes from busy to idle, you get the callback from your background task that the
operation has finished, you have to callonTransitionToIdle() on the registered ResourceCallback.

4) In getName() you have to return the name of the resource which is used for logging.
 *
 * @property view
 * @property expectedVisibility
 */
class ViewVisibilityIdlingResource(
    private val view: View,
    private val expectedVisibility: Int
) : IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null
    private var isIdle: Boolean = false

    override fun getName(): String {
        return ViewVisibilityIdlingResource::class.java.name +
                ":" + view.id + ":" + expectedVisibility
    }

    override fun isIdleNow(): Boolean {
        if (isIdle) return true

        isIdle = view.visibility == expectedVisibility

        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }

        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}