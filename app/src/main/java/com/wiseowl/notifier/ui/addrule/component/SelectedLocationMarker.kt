package com.wiseowl.notifier.ui.addrule.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun SelectedLocationMarker(name: String?, updatedPosition: LatLng) {
    val markerState = remember { MarkerState(position = updatedPosition) }

    // Update marker position dynamically
    LaunchedEffect(updatedPosition) {
        markerState.position = updatedPosition
    }

    Marker(
        state = markerState,
        title = name,
    )
}
