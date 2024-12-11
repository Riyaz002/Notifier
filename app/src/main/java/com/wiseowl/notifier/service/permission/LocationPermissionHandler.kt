package com.wiseowl.notifier.service.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class LocationPermissionHandler: PermissionHandler {
    override fun isGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
    }

    override fun askPermission(): Result<PermissionResult> {
        TODO("Not yet implemented")
    }
}