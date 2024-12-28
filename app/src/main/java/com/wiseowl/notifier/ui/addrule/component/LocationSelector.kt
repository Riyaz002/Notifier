package com.wiseowl.notifier.ui.addrule.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.wiseowl.notifier.domain.event.EventHandler
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.ui.Event
import com.wiseowl.notifier.ui.ProgressBar
import com.wiseowl.notifier.ui.addrule.model.AddRuleEvent
import com.wiseowl.notifier.ui.addrule.model.Suggestion
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelector(
    modifier: Modifier = Modifier,
    value: String = "",
    label: String = "Location",
    suggestions: List<Suggestion> = listOf(),
    placeName: String?,
    selectedLocation: Location?,
    radius: Double?,
    error: String? = null,
    onEvent: KFunction1<Event, Unit>,
) {
    var suggestionExpanded by remember {
        mutableStateOf(suggestions.isNotEmpty())
    }
    val scope = rememberCoroutineScope()
    val cameraPosition = rememberCameraPositionState {
        if (selectedLocation != null) builder().target(
            LatLng(
                selectedLocation.latitude,
                selectedLocation.longitude
            )
        ).zoom(0f)
    }
    val effectedArea by rememberUpdatedState(newValue = radius)
    val transitionAnimationState = animateFloatAsState(targetValue = effectedArea?.toFloat() ?: 0f)
    LaunchedEffect(key1 = selectedLocation) {
        if (selectedLocation == null) {
            EventHandler.send(ProgressBar(true))
        } else EventHandler.send(ProgressBar(false))
        scope.launch {
            if (selectedLocation != null) {
                cameraPosition.animate(
                    update = CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            selectedLocation.latitude,
                            selectedLocation.longitude
                        ), 15f
                    ),
                    durationMs = 2000
                )
            }
        }
    }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = true),
        onDismissRequest = { onEvent(AddRuleEvent.CloseLocationDialog) }) {
        Box {
            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = suggestions.isNotEmpty(),
                    onExpandedChange = { }) {
                    OutlinedTextField(
                        value = value,
                        shape = AbsoluteCutCornerShape(0.dp),
                        singleLine = true,
                        label = { Text(label) },
                        onValueChange = { onEvent(AddRuleEvent.OnQueryPlace(it)) },
                        isError = !error.isNullOrEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .menuAnchor()
                    )

                    DropdownMenu(
                        modifier = Modifier
                            .wrapContentSize()
                            .heightIn(max = 250.dp)
                            .exposedDropdownSize(),
                        expanded = suggestions.isNotEmpty(),
                        properties = PopupProperties(focusable = false, dismissOnBackPress = true),
                        onDismissRequest = { suggestionExpanded = false }
                    ) {
                        suggestions.forEach { suggestion ->
                            DropdownMenuItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .border(1.dp, color = Color.Black)
                                    .padding(10.dp),
                                text = { Text(text = buildString { append(suggestion.fullText) }) },
                                onClick = { onEvent(AddRuleEvent.OnSuggestionSelected(suggestion)) }
                            )
                        }
                    }
                }

                GoogleMap(
                    modifier = Modifier.fillMaxHeight(0.8f),
                    cameraPositionState = cameraPosition,
                    onMapClick = {
                        onEvent(
                            AddRuleEvent.OnChangeRuleLocation(
                                Location(
                                    it.longitude,
                                    it.latitude
                                )
                            )
                        )
                    }
                ) {
                    if (selectedLocation != null) {
                        val latLong = LatLng(selectedLocation.latitude, selectedLocation.longitude)
                        SelectedLocationMarker(name = placeName, updatedPosition = latLong)
                        effectedArea?.let {
                            Circle(
                                center = latLong,
                                fillColor = Color.Red.copy(alpha = 0.3f),
                                radius = transitionAnimationState.value.toDouble(),
                                strokeWidth = 0f
                            )
                        }
                    }
                }
                Slider(
                    value = radius!!.toFloat(),
                    onValueChange = { onEvent(AddRuleEvent.OnChangeRuleRadius(it.toString())) },
                    valueRange = 15f.rangeTo(1000f)
                )
            }
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
                    .align(Alignment.BottomCenter),
                onClick = { onEvent(AddRuleEvent.CloseLocationDialog) },
            ) { Text(text = "Confirm Selection", color = Color.White) }
        }
    }
}