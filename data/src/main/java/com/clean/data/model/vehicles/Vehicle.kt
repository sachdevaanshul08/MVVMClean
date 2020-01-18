package com.clean.data.model.vehicles

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "vehicle", primaryKeys = ["id","index"])
data class Vehicle(
    @SerializedName("coordinate")
    @Expose
    @ColumnInfo(name = "coordinate")
    val coordinate: Coordinate,
    @SerializedName("fleetType")
    @Expose
    @ColumnInfo(name = "fleetType")
    val fleetType: String,
    @SerializedName("heading")
    @Expose
    @ColumnInfo(name = "heading")
    val heading: Double,
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    val id: Int,

    var index: Int
)