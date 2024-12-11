package com.wiseowl.notifier.ui.addrule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiseowl.notifier.data.ServiceLocator
import com.wiseowl.notifier.domain.event.SnackBarEvent
import com.wiseowl.notifier.ui.addrule.model.AddRuleEvent
import com.wiseowl.notifier.ui.addrule.model.AddRuleState
import com.wiseowl.notifier.ui.navigation.Navigator
import com.wiseowl.notifier.ui.navigation.Pop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddRuleViewModel: ViewModel() {
    private val _state = MutableStateFlow(AddRuleState())
    val state: StateFlow<AddRuleState> get() = _state

    fun onEvent(event: AddRuleEvent){
        when(event){
            is AddRuleEvent.CreateRule -> {
                viewModelScope.launch{
                    ServiceLocator.getRulesRepository().addRule(event.rule)
                    Navigator.popBackStack(Pop())
                    SnackBarEvent.send("Rule added successfully")
                }
            }
            is AddRuleEvent.OnChangeRuleActionType -> _state.update { state -> state.copy( ruleType = state.ruleType.copy(event.actionType)) }
            is AddRuleEvent.OnChangeRuleDelay -> _state.update { state -> state.copy( ruleDelay = state.ruleDelay.copy(event.delayInMinutes)) }
            is AddRuleEvent.OnChangeRuleDescription -> _state.update { state -> state.copy( ruleDescription = state.ruleDescription.copy(event.description))}
            is AddRuleEvent.OnChangeRuleLocation -> _state.update { state -> state.copy( location = state.location?.copy(event.location)) }
            is AddRuleEvent.OnChangeRuleName -> _state.update { state -> state.copy( ruleName = state.ruleName.copy(event.name)) }
            is AddRuleEvent.OnChangeRuleRadius -> _state.update { state -> state.copy( ruleRadius = state.ruleRadius.copy(event.radiusInMeters)) }
        }
    }
}