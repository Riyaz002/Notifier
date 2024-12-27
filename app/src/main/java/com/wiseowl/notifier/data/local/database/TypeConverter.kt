package com.wiseowl.notifier.data.local.database

import androidx.room.TypeConverter
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.Place

object TypeConverter {
    @TypeConverter
    fun fromPlace(place: Place): String{
        return "${place.name}:${place.location.latitude}:${place.location.longitude}"
    }

    @TypeConverter
    fun toPlace(place: String): Place{
        return place.split(":").run {
            Place(get(0), Location(get(1).toDouble(), get(2).toDouble()))
        }
    }

    @TypeConverter
    fun fromLocation(location: Location): String{
        return "${location.latitude}:${location.longitude}"
    }

    @TypeConverter
    fun toLocation(location: String): Location{
        return location.split(":").run {
            Location(first().toDouble(), last().toDouble())
        }
    }
}