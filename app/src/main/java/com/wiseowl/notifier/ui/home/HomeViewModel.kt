package com.wiseowl.notifier.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiseowl.notifier.data.ServiceLocator
import com.wiseowl.notifier.ui.home.model.HomeEvent
import com.wiseowl.notifier.ui.home.model.HomeState
import com.wiseowl.notifier.ui.navigation.AddRule
import com.wiseowl.notifier.ui.navigation.Navigate
import com.wiseowl.notifier.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> get() = _state

    init {
        viewModelScope.launch {
            ServiceLocator.getUserRepository().getUser().let{
                _state.update { state -> state.copy(user = it) }
            }
            ServiceLocator.getRulesRepository().getRules().collect{
                _state.update { state -> state.copy(rules = it) }
            }
        }
    }

    fun onEvent(event: HomeEvent){
        when(event){
            HomeEvent.CreateRule -> Navigator.navigate(Navigate(AddRule))
            is HomeEvent.DeleteRule -> viewModelScope.launch {
                ServiceLocator.getRulesRepository().deleteRule(event.ruleId)
            }
        }
    }
}