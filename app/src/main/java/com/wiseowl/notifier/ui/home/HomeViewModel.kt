package com.wiseowl.notifier.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiseowl.notifier.data.di.ServiceLocator
import com.wiseowl.notifier.domain.event.EventManager
import com.wiseowl.notifier.domain.event.Event
import com.wiseowl.notifier.ui.home.model.HomeEvent
import com.wiseowl.notifier.ui.home.model.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> get() = _state

    init {
        viewModelScope.launch {
            ServiceLocator.getUserRepository().getUser().collect{
                _state.update { state -> state.copy(user = it) }
            }
        }

        viewModelScope.launch {
            ServiceLocator.getRulesRepository().getRules().collect {
                _state.update { state -> state.copy(rules = it) }
            }
        }
    }

    fun onEvent(event: Event){
        when(event){
            is HomeEvent.DeleteRule -> viewModelScope.launch {
                ServiceLocator.getRulesRepository().deleteRule(event.ruleId)
            }
            else -> EventManager.send(event)
        }
    }
}