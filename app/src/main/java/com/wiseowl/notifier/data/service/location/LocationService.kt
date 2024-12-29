package com.wiseowl.notifier.data.service.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.wiseowl.notifier.domain.model.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

const val LOCATION_FETCH_BACKOFF_TIME_MILLS = 1*60*1000

class LocationService private constructor(context: Context) {
    val coroutineScope = CoroutineScope(SupervisorJob())
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private var lastFetchedLocation: Location? = null
    private var lastFetchedTime: Long = 0

    suspend fun getCurrentLocation(context: Context, dispatcher: CoroutineDispatcher): Location? {
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) throw SecurityException("Location permission not granted")
        if(lastFetchedLocation!=null && (System.currentTimeMillis()-lastFetchedTime)<LOCATION_FETCH_BACKOFF_TIME_MILLS){
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

    fun getLocationUpdates(context: Context, dispatcher: CoroutineDispatcher): Flow<Location> {
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) throw SecurityException("Location permission not granted")
        return callbackFlow {
            fusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5000).build(),
                object : LocationCallback(){
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        val longitude = result.lastLocation?.longitude?:return
                        val latitude = result.lastLocation?.latitude?:return
                        coroutineScope.launch(dispatcher) {
                            send(Location(longitude, latitude))
                        }
                    }
                },
                null)
            awaitClose()
        }
    }

    companion object{
        @Volatile private var INSTANCE: LocationService? = null

        fun getInstance(context: Context): LocationService{
            return INSTANCE ?: synchronized(this){
                INSTANCE = LocationService(context)
                INSTANCE!!
            }
        }
    }
}