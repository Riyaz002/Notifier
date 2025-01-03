package com.wiseowl.notifier.data.service.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.service.LocationService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlin.jvm.Throws

const val LOCATION_FETCH_BACKOFF_TIME_MILLS = 1*60*1000

class LocationService(context: Context): LocationService {
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private var lastFetchedLocation: Location? = null
    private var lastFetchedTime: Long = 0

    /**
     * get current location with a back of time of [LOCATION_FETCH_BACKOFF_TIME_MILLS].
     */
    @Throws(SecurityException::class)
    override suspend fun getCurrentLocation(context: Context, dispatcher: CoroutineDispatcher): Location? {
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) throw SecurityException("Location permission not granted")
        if(lastFetchedLocation!=null && (System.currentTimeMillis()-lastFetchedTime) < LOCATION_FETCH_BACKOFF_TIME_MILLS){
            return lastFetchedLocation
        }
        val result = fusedLocationProviderClient.getCurrentLocation(
            CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY).build(),
            null
        ).await()
        if(result==null) return null
        val longitude = result.longitude
        val latitude = result.latitude
        lastFetchedLocation = Location(longitude = longitude, latitude = latitude)
        return lastFetchedLocation
    }
}