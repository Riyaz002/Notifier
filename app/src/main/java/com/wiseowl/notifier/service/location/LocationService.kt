package com.wiseowl.notifier.service.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.service.permission.LocationPermissionHandler
import kotlinx.coroutines.tasks.await

class LocationService(context: Context) {
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): Location {
        if(!LocationPermissionHandler().isGranted(context)) throw SecurityException("Location permission not granted")
        val result = fusedLocationProviderClient.getCurrentLocation(CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY).build(), null).await()
        return Location(result.longitude, result.longitude)
    }
}