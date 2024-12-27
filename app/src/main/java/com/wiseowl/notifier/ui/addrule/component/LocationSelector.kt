package com.wiseowl.notifier.ui.addrule.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.wiseowl.notifier.R
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.ui.Event
import com.wiseowl.notifier.ui.addrule.model.AddRuleEvent
import com.wiseowl.notifier.ui.addrule.model.Suggestion
import kotlin.reflect.KFunction1


@Composable
fun LocationSelector(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "Location",
    suggestions: List<Suggestion> = listOf(),
    currentLocation: Location,
    error: String? = null,
    onEvent: KFunction1<Event, Unit>
) {
    var suggestionExpanded by remember{
        mutableStateOf(suggestions.isNotEmpty())
    }

    var mapExpanded by remember{
        mutableStateOf(suggestions.isNotEmpty())
    }

    val cameraPositionStatePosition = rememberCameraPositionState{
        builder().target(LatLng(currentLocation.latitude, currentLocation.longitude)).zoom(100f)
    }

    LaunchedEffect(key1 = currentLocation) {
        Log.e("TEST", "Current: $currentLocation")
    }


    Column {
        OutlinedTextField(
            value = value,
            shape = AbsoluteCutCornerShape(0.dp),
            singleLine = true,
            label = { Text(label) },
            onValueChange = { onEvent(AddRuleEvent.OnQueryPlaces(it)) },
            isError = !error.isNullOrEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .onFocusEvent {
                    mapExpanded = it.isFocused
                }
        )
        DropdownMenu(modifier = Modifier.fillMaxWidth(), expanded = suggestions.isNotEmpty(), onDismissRequest = { suggestionExpanded = false }) {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, color = Color.Black)
                    .padding(10.dp),
                    text = { Text(text = buildString { append(suggestion.fullText) }) },
                    onClick = {
                        onEvent(AddRuleEvent.OnSuggestionSelected(suggestion))
                    }
                )
            }
        }
    }

    Text(modifier = Modifier.fillMaxWidth(), text = "Or, Pinpoint on map", textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)

    GoogleMap(
        modifier = Modifier.height(if (mapExpanded) 200.dp else 56.dp).fillMaxSize(),
        cameraPositionState = cameraPositionStatePosition,
        onMapClick = { onEvent(AddRuleEvent.OnChangeRuleLocation(Location(it.longitude, it.latitude))) }
    ){
        Marker(
            state = rememberMarkerState(position = LatLng(currentLocation.latitude, currentLocation.longitude)),
            title = "Current Location",
            tag = "location",
            icon = BitmapDescriptorFactory.fromResource(R.color.teal_200),
            onClick = { marker ->
                false
            }
        )
    }
}