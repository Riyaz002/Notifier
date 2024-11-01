package com.wiseowl.notifier.data

import androidx.room.TypeConverter
import com.wiseowl.notifier.domain.model.Location

object TypeConverter {
    @TypeConverter
    fun fromLocation(location: Location): String{
        return location.latitude.toString()+":"+location.longitude.toString()
    }

    @TypeConverter
    fun toLocation(location: String): Location{
        return location.split(":").run {
            Location(first().toFloat(), last().toFloat())
        }
    }
}