package com.clean.data.api

import androidx.lifecycle.LiveData
import com.clean.data.constant.Constants
import com.clean.data.model.vehicles.Vehicles
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface VehicleApi {

    @GET("/")
    fun getVehicles(
        @Header(Constants.REQUEST_HEADER_TAG) tag: String, @Query("p1Lat") p1Lat: Double,
        @Query("p1Lon") p1Lon: Double, @Query("p2Lat") p2Lat: Double, @Query("p2Lon") p2Lon: Double
    ): LiveData<ApiResponse<Vehicles>>
}