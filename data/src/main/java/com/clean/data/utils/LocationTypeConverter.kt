package com.clean.data.utils

import androidx.room.TypeConverter
import com.clean.data.model.vehicles.Coordinate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocationTypeConverter {

    /**
     * Convert a a list of Locations data to a Json
     */
    @TypeConverter
    fun fromLocation(stat: Coordinate): String {
        return Gson().toJson(stat)
    }

    /**
     * Convert a json to a list of Locations data
     */
    @TypeConverter
    fun toLocation(jsonLocation: String): Coordinate {
        val notesType = object : TypeToken<Coordinate>() {}.type
        return Gson().fromJson<Coordinate>(jsonLocation, notesType)
    }
}