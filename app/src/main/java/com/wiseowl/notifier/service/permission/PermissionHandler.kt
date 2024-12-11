package com.wiseowl.notifier.service.permission

import android.content.Context

interface PermissionHandler {
    fun isGranted(context: Context): Boolean
    fun askPermission(): Result<PermissionResult>
}

