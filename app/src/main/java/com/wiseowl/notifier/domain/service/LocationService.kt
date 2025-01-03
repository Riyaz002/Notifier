package com.wiseowl.notifier.domain.service

import android.content.Context
import com.wiseowl.notifier.data.service.location.LOCATION_FETCH_BACKOFF_TIME_MILLS
import com.wiseowl.notifier.domain.model.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.jvm.Throws
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

interface LocationService {
    /**
     * get current location with a back of time of [LOCATION_FETCH_BACKOFF_TIME_MILLS].
     */
    @Throws(SecurityException::class)
    suspend fun getCurrentLocation(context: Context, dispatcher: CoroutineDispatcher): Location?

    companion object{
        /**
         * Calculate distance between 2 location coordinates
         */
        fun getDistanceFromLatLonInMeters(location1: Location, location2: Location): Double {
            val R = 6371 // Radius of the earth in km
            val dLat = deg2rad(location2.latitude - location1.latitude)
            val dLon = deg2rad(location2.longitude - location1.longitude)
            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(deg2rad(location1.latitude)) * cos(deg2rad(location2.latitude)) *
                    sin(dLon / 2) * sin(dLon / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return R * c * 1000 // Distance in km
        }

        private fun deg2rad(deg: Double): Double {
            return deg * (Math.PI / 180)
        }
    }
}