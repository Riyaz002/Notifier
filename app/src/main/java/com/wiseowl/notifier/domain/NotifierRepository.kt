package com.wiseowl.notifier.domain

import com.wiseowl.notifier.domain.model.Rule
import kotlinx.coroutines.flow.Flow

interface NotifierRepository {
    fun getRules(): Flow<List<Rule>>
    suspend fun addRule(rule: Rule)
    suspend fun updateRule(rule: Rule)
    suspend fun deleteRule(id: Int)
}