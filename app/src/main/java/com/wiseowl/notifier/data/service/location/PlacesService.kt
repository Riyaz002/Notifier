package com.wiseowl.notifier.data.service.location

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.wiseowl.notifier.BuildConfig.MAPS_API_KEY
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.ui.addrule.model.Suggestion
import kotlinx.coroutines.tasks.await


class PlacesService(context: Context) {
    init {
        Places.initializeWithNewPlacesApiEnabled(context, MAPS_API_KEY)
    }

    private val placesClient = Places.createClient(context)

    suspend fun getNearbyPlacesForQuery(query: String, location: Location): List<Suggestion>? {
        val center = LatLng(location.latitude, location.longitude);
        val circle = CircularBounds.newInstance(center, /* radius = */ 5000.0);
        val autocompletePlacesRequest =
            FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setLocationRestriction(circle)
                .build()
        val result = placesClient.findAutocompletePredictions(autocompletePlacesRequest).await()
        return if(result.autocompletePredictions.isNotEmpty()){
            result.autocompletePredictions.map {
                Suggestion(
                    it.placeId,
                    it.getFullText(null),
                    it.getPrimaryText(null),
                    it.getSecondaryText(null)
                )
            }
        } else null
    }

    suspend fun getPlaceDetail(placeId: String): Location? {
        val fields = listOf(Place.Field.LOCATION)
        val request = FetchPlaceRequest.newInstance(placeId, fields)
        val location = placesClient.fetchPlace(request).await().place.location
        return if(location != null) {
            Location(location.longitude, location.latitude)
        } else null
    }
}