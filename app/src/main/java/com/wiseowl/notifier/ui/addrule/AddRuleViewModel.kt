package com.wiseowl.notifier.ui.addrule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiseowl.notifier.data.ServiceLocator
import com.wiseowl.notifier.domain.event.EventHandler
import com.wiseowl.notifier.domain.model.Rule
import com.wiseowl.notifier.domain.util.RuleValidator
import com.wiseowl.notifier.ui.Event
import com.wiseowl.notifier.ui.PopBackStack
import com.wiseowl.notifier.ui.SnackBar
import com.wiseowl.notifier.ui.addrule.model.AddRuleEvent
import com.wiseowl.notifier.ui.addrule.model.AddRuleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddRuleViewModel: ViewModel() {
    private val _state = MutableStateFlow(AddRuleState())
    val state: StateFlow<AddRuleState> get() = _state

    fun onEvent(event: Event){
        when(event){
            is AddRuleEvent.CreateRule -> {
                viewModelScope.launch{
                    val value = state.value
                    val rule = Rule(
                        name = value.ruleName.value!!,
                        description = value.ruleDescription.value,
                        place = value.place.value!!,
                        radiusInMeter = value.ruleRadius.value!!,
                        actionType = value.actionType.value!!,
                        active = false,
                        delayInMinutes = value.ruleDelay.value!!
                    )
                    with(RuleValidator(rule)){
                        if(isRuleValid()){
                            ServiceLocator.getRulesRepository().addRule(rule)
                            EventHandler.send(PopBackStack)
                            EventHandler.send(SnackBar("Rule added successfully"))
                        } else{
                            if(rule.name.isEmpty()) _state.update { it.copy(ruleName = it.ruleName.copy(error = "Name cannot be empty")) }
                            if(rule.place == null) _state.update { it.copy(place = it.place.copy(error = "Please choose an effecting place")) }
                            if(rule.actionType == null) _state.update { it.copy(actionType = it.actionType.copy(error = "Please choose an action type")) }
                            if(rule.radiusInMeter<1) _state.update { it.copy(ruleRadius = it.ruleRadius.copy(error = "radius should be greater than 0")) }
                            if(rule.delayInMinutes<1) _state.update { it.copy(ruleDelay = it.ruleDelay.copy(error = "delay should be greater than 0")) }
                        }
                    }
                }
            }
            is AddRuleEvent.OnChangeRuleActionType -> _state.update { state -> state.copy( actionType = state.actionType.copy(event.actionType)) }
            is AddRuleEvent.OnChangeRuleDelay -> _state.update { state -> state.copy( ruleDelay = state.ruleDelay.copy(event.delayInMinutes)) }
            is AddRuleEvent.OnChangeRuleDescription -> _state.update { state -> state.copy( ruleDescription = state.ruleDescription.copy(event.description))}
            is AddRuleEvent.OnChangeRulePlace -> _state.update { state -> state.copy(place = state.place.copy(event.place)) }
            is AddRuleEvent.OnChangeRuleName -> _state.update { state -> state.copy( ruleName = state.ruleName.copy(event.name)) }
            is AddRuleEvent.OnChangeRuleRadius -> _state.update { state -> state.copy( ruleRadius = state.ruleRadius.copy(event.radiusInMeters)) }
            else -> EventHandler.send(event)
        }
    }
}