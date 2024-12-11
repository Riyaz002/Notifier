package com.wiseowl.notifier.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiseowl.notifier.domain.RulesRepository
import com.wiseowl.notifier.domain.model.Rule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SharedViewModel(private val repository: RulesRepository): ViewModel() {
    private val rules: Flow<List<Rule>> = repository.getRules()
    private val _rules = rules.asLiveData()

    fun addRule(rule: Rule){
        viewModelScope.launch {
            repository.addRule(rule)
        }
    }

    fun editRule(rule: Rule){
        viewModelScope.launch {
            repository.addRule(rule)
        }
    }

    fun deleteRule(id: Int){
        viewModelScope.launch {
            repository.deleteRule(id)
        }
    }

    fun getRuleById(id: Int): Rule?{
        return _rules.value?.first { it.id == id }
    }
}