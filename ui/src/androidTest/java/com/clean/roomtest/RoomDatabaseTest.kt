package com.clean.roomtest

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.clean.data.db.dao.vehicles.VehiclesDao
import com.clean.data.db.database.AppDatabase
import com.clean.data.model.vehicles.Vehicle
import com.clean.data.model.vehicles.Vehicles
import com.clean.utils.ReaderUtil.asset
import com.google.gson.Gson
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    private lateinit var vehiclesDao: VehiclesDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        vehiclesDao = db.getVehiclesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun writeToDatabaseTest() {
        val vehicles: Vehicles = Gson().fromJson(
            asset(InstrumentationRegistry.getInstrumentation().context, "sample.json"),
            Vehicles::class.java
        )
        vehiclesDao.insert(vehicles.vehicleList)
        var count = -1
        vehiclesDao.vehicleCount().observeForever { count = it }
        assertTrue("Vehicle Table has some rows", count.compareTo(0) != 0);
    }

    @Test
    @Throws(Exception::class)
    fun ReadFromDatabase() {
        vehiclesDao.delete()
        val vehicles: Vehicles = Gson().fromJson(
            asset(InstrumentationRegistry.getInstrumentation().context, "sample.json"),
            Vehicles::class.java
        )
        db.runInTransaction {
            val start = 0
            val items = vehicles.vehicleList.mapIndexed { pointer, child ->
                child.index = start + pointer
                child
            }
            vehiclesDao.insert(items)
        }
        var data: List<Vehicle>? = null
        vehiclesDao.getDataRange(0, 2).observeForever { data = it }
        assertTrue("Fetch first two rows from vehicle table successfully", data?.size == 2);
    }


    @Test
    @Throws(Exception::class)
    fun deleteData() {
        var count = -1
        val vehicles: Vehicles = Gson().fromJson(
            asset(InstrumentationRegistry.getInstrumentation().context, "sample.json"),
            Vehicles::class.java
        )
        vehiclesDao.insert(vehicles.vehicleList)
        vehiclesDao.vehicleCount().observeForever { count = it }
        assertTrue("Vehicle Table has some rows", count.compareTo(0) != 0);
        vehiclesDao.delete()
        vehiclesDao.vehicleCount().observeForever { count = it }
        assertTrue("Vehicle Table rows deleted successfully", count.compareTo(0) == 0);

    }


}