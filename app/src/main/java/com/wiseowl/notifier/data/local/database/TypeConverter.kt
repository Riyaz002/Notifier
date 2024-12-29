package com.wiseowl.notifier.data.local.database

import androidx.room.TypeConverter
import com.wiseowl.notifier.domain.model.Location

object TypeConverter {
    @TypeConverter
    fun fromLocation(location: Location): String{
        return "${location.longitude}:${location.latitude}"
    }

    @TypeConverter
    fun toLocation(location: String): Location{
        return location.split(":").run {
            Location(first().toDouble(), last().toDouble())
        }
    }
}