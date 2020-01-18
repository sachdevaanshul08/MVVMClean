package com.clean.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.clean.data.BuildConfig
import com.clean.data.db.dao.vehicles.VehiclesDao
import com.clean.data.model.vehicles.Vehicle
import com.clean.data.utils.LocationTypeConverter

@Database(entities = [Vehicle::class], version = BuildConfig.DATABASE_VERSION, exportSchema = false)
@TypeConverters(LocationTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getVehiclesDao(): VehiclesDao
}