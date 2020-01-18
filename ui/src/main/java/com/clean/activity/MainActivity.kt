package com.clean.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.test.espresso.idling.CountingIdlingResource
import com.clean.R
import com.clean.base.BaseFragment
import com.clean.databinding.ActivityMainBinding
import com.clean.homescreen.HomeFragment
import dagger.android.support.DaggerAppCompatActivity


class MainActivity : DaggerAppCompatActivity() {

    // this idling resource will be used by Espresso to wait for and synchronize with RetroFit Network call
    private var espressoTestIdlingResource: CountingIdlingResource = CountingIdlingResource("Background_op")

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        showBackButton(true)

        initListeners(binding)

        if (savedInstanceState == null) {
            openFragment(HomeFragment.newInstance())
        }
    }

    private fun initListeners(binding: ActivityMainBinding) =
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragment)
            .addToBackStack(null).commit()
    }

    fun isBackButtonRequired(): Boolean {
        return supportFragmentManager.backStackEntryCount > 1
    }

    /**
     *  To show the back button on screen
     */
    fun showBackButton(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }


    fun showProgressBar(boolean: Boolean) {
        binding.frameProgressbar.visibility = if (boolean) View.VISIBLE else View.GONE
    }

    /**
     * This will call @visibleAgain on fragment, which will be visible AGAIN on
     * pressing back button
     *
     */
    private fun triggerFragmentVisible() {
        val fragmentList = supportFragmentManager.fragments
        val listIterator = fragmentList.listIterator(fragmentList.size)
        // Iterate in reverse.
        while (listIterator.hasPrevious()) {
            val fragment = listIterator.previous()
            if (fragment is BaseFragment<*, *>) {
                fragment.visibleAgain()
                break
            }
        }
    }

    override fun onBackPressed() {
        if (isBackButtonRequired()) {
            super.onBackPressed()
            showBackButton(isBackButtonRequired())
            triggerFragmentVisible()
        } else {
            finish()
        }
    }

    internal fun incrementCountingIdlingResource() {
        espressoTestIdlingResource.increment()
    }

    internal fun decrementCountingIdlingResource() {
        espressoTestIdlingResource.decrement()
    }

    /**
     *
     * @return MainActvity's idling resource for Espresso testing
     */
    fun getEspressoIdlingResourceForMainActivity(): CountingIdlingResource {
        return espressoTestIdlingResource
    }
}
