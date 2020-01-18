package com.clean.mapfragmenttest

import android.content.Context
import android.os.Bundle
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.clean.BaseTest
import com.clean.R
import com.clean.activity.MainActivity
import com.clean.data.db.dao.vehicles.VehiclesDao
import com.clean.data.db.database.AppDatabase
import com.clean.data.model.vehicles.Vehicles
import com.clean.mapscreen.MapDetailFragment
import com.clean.utils.APICallsSuccessDispatcher
import com.clean.utils.ReaderUtil
import com.google.gson.Gson
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MapFragmentViewTest : BaseTest() {
    @JvmField
    @Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, false, true)

    private var mapIdlingResource: CountingIdlingResource? = null

    private lateinit var vehiclesDao: VehiclesDao
    private lateinit var db: AppDatabase

    override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        vehiclesDao = db.getVehiclesDao()
    }

    override fun isMockServerEnabled(): Boolean {
        return true
    }

    override fun tearDown() {
        super.tearDown()
        IdlingRegistry.getInstance().unregister(mapIdlingResource)
    }


    @Test
    fun checkIfMapIsDisplayed() {

        val vehicles: Vehicles = Gson().fromJson(
            ReaderUtil.asset(InstrumentationRegistry.getInstrumentation().context, "sample.json"),
            Vehicles::class.java
        )
        vehiclesDao.insert(vehicles.vehicleList)
        setupFragment()
        mapIdlingResource = activityTestRule.activity.getEspressoIdlingResourceForMainActivity()
        mockServer.setDispatcher(APICallsSuccessDispatcher())
        MapFragmentHelper()
            .waitForCondition(mapIdlingResource)
            .assertMapVisible()
    }

    private fun setupFragment() {
        val fragmentManager = activityTestRule.activity.supportFragmentManager
        val vehicleId = "739330"  //ID taken from "sample.json"
        val fragmentArgs = Bundle().apply {
            putString(MapDetailFragment.MAP_FRAGMENT_BUNDLE_KEY, vehicleId)
        }
        var fragment = MapDetailFragment.newInstance()
        fragment.arguments = fragmentArgs
        fragmentManager.beginTransaction()
            .add(R.id.frame_container, fragment)
            .commit()

        // Wait for the fragment to be committed
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()
    }

}