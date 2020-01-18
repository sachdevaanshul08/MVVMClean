package com.clean.data.db.dao.vehicles

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.clean.data.model.vehicles.Vehicle

@Dao
interface VehiclesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deliveryData: List<Vehicle>)

    @Query("SELECT * FROM vehicle LIMIT :limit OFFSET :offset")
    fun getDataRange(offset: Int, limit: Int): LiveData<List<Vehicle>>

    @Query("SELECT COUNT(*) from vehicle")
    fun vehicleCount(): LiveData<Int>

    @Query("DELETE FROM vehicle")
    fun delete()
}