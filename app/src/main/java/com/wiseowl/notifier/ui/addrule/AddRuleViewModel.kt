package com.wiseowl.notifier.ui.addrule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiseowl.notifier.data.di.ServiceLocator
import com.wiseowl.notifier.domain.event.EventHandler
import com.wiseowl.notifier.domain.model.Rule
import com.wiseowl.notifier.domain.util.RuleValidator
import com.wiseowl.notifier.ui.Event
import com.wiseowl.notifier.ui.PopBackStack
import com.wiseowl.notifier.ui.ProgressBar
import com.wiseowl.notifier.ui.SnackBar
import com.wiseowl.notifier.ui.addrule.model.AddRuleEvent
import com.wiseowl.notifier.ui.addrule.model.AddRuleState
import com.wiseowl.notifier.ui.addrule.model.AddRuleUIEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class AddRuleViewModel: ViewModel() {
    private val _state = MutableStateFlow(AddRuleState())
    val state: StateFlow<AddRuleState> get() = _state

    private val _uiEvent = Channel<AddRuleUIEvent> {  }
    val uiEvent = _uiEvent.consumeAsFlow()

    fun onEvent(event: Event){
        when(event){
            is AddRuleEvent.OnClickActionType -> _state.update { state -> state.copy(actionTypeSelectorExpandedState = true) }
            is AddRuleEvent.OnClickRepeatType -> _state.update { state -> state.copy(repeatTypeSelectorExpandedState = true) }
            is AddRuleEvent.OnChangeRuleActionType -> _state.update { state -> state.copy( actionType = state.actionType.copy(event.actionType), actionTypeSelectorExpandedState = false) }
            is AddRuleEvent.OnChangeRuleRepeatType -> _state.update { state -> state.copy( repeatType = state.repeatType.copy(event.repeatType), repeatTypeSelectorExpandedState = false) }
            is AddRuleEvent.OnChangeRuleDescription -> _state.update { state -> state.copy( ruleDescription = state.ruleDescription.copy(event.description))}
            is AddRuleEvent.OnClickSelectLocationField -> _state.update { state -> state.copy(locationSelectorExpandedState = true) }
            is AddRuleEvent.CloseLocationDialog -> _state.update { state -> state.copy(locationSelectorExpandedState = false) }
            is AddRuleEvent.OnChangeRuleName -> _state.update { state -> state.copy( ruleName = state.ruleName.copy(event.name)) }
            is AddRuleEvent.OnChangeRuleLocation -> _state.update { state -> state.copy(selectedPlaceLocation = event.location) }
            is AddRuleEvent.OnPlaceSuggestionUpdated -> _state.update { state -> state.copy(suggestions = event.suggestions.orEmpty()) }
            is AddRuleEvent.OnChangeRuleDelay -> {
                val newDelay = event.delayInMinutes.toDoubleOrNull()?.toInt() ?: return
                _state.update { state -> state.copy( ruleDelay = state.ruleDelay.copy(newDelay)) }
            }
            is AddRuleEvent.OnChangeRuleRadius -> {
                val newRadius = event.radiusInMeters.toDoubleOrNull()?.roundToInt()?.toDouble()
                _state.update { state -> state.copy( ruleRadius = state.ruleRadius.copy(newRadius)) }
            }
            is AddRuleEvent.OnQueryPlace -> viewModelScope.launch {
                _state.update { state -> state.copy(selectedPlaceName = state.selectedPlaceName.updateValue(event.search)) }
                _uiEvent.send(AddRuleUIEvent.SearchPlace(event.search))
            }
            is AddRuleEvent.OnSuggestionSelected -> {
                viewModelScope.launch(Dispatchers.IO) {
                    EventHandler.send(ProgressBar(true))
                    val placeLocation = ServiceLocator.getPlacesService().getPlaceDetail(event.suggestion.placeId)
                    _state.update { state -> state.copy(selectedPlaceLocation = placeLocation, suggestions = listOf()) }
                    _state.update { state -> state.copy(selectedPlaceName = state.selectedPlaceName.updateValue(event.suggestion.fullText.toString()), suggestions = listOf()) }
                    EventHandler.send(ProgressBar(false))
                }
            }
            is AddRuleEvent.CreateRule -> {
                viewModelScope.launch{
                    val value = state.value
                    if(value.ruleName.value.isNullOrEmpty()) {
                        _state.update { it.copy(ruleName = it.ruleName.copy(error = "Name cannot be empty")) }
                        return@launch
                    }
                    if(value.selectedPlaceLocation == null) {
                        _state.update { it.copy(selectedPlaceName = it.selectedPlaceName.copy(error = "Please choose an effecting place")) }
                        return@launch
                    }
                    if(value.ruleRadius.value==null ||  value.ruleRadius.value<1) {
                        _state.update { it.copy(ruleRadius = it.ruleRadius.copy(error = "radius should be greater than 0")) }
                        return@launch
                    }
                    if(value.ruleDelay.value==null || value.ruleDelay.value<1) {
                        _state.update { it.copy(ruleDelay = it.ruleDelay.copy(error = "delay should be greater than 0")) }
                        return@launch
                    }

                    val rule = Rule(
                        name = value.ruleName.value,
                        description = value.ruleDescription.value,
                        location = value.selectedPlaceLocation,
                        radiusInMeter = value.ruleRadius.value,
                        actionType = value.actionType.value!!,
                        active = true,
                        repeatType = value.repeatType.value,
                        delayInMinutes = value.ruleDelay.value
                    )
                    with(RuleValidator(rule)){
                        if(isRuleValid()){
                            ServiceLocator.getRulesRepository().addRule(rule)
                            EventHandler.send(PopBackStack)
                            EventHandler.send(SnackBar("Rule added successfully"))
                        } else throw Exception("Creating rule with invalid parameters")
                    }
                }
            }
            else -> EventHandler.send(event)
        }
    }
}