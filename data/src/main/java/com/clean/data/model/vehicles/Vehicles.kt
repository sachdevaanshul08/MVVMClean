package com.clean.data.model.vehicles

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Vehicles(
    @SerializedName("poiList")
    @Expose
    val vehicleList: List<Vehicle>
)