package com.clean.data.model.vehicles

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Coordinate(
    @SerializedName("latitude")
    @Expose
    val latitude: Double,
    @SerializedName("longitude")
    @Expose
    val longitude: Double
)