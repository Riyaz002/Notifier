package com.wiseowl.notifier.data.repository

import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.RepeatType
import com.wiseowl.notifier.domain.model.Rule
import com.wiseowl.notifier.domain.repository.RulesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RulesRepositoryImpl : RulesRepository {
    private val rules = arrayListOf(
        Rule(
            0,
            "Name",
            "Description",
            Location(77.0, 28.13),
            radiusInMeter = 100.0,
            true,
            ActionType.ENTERING,
            RepeatType.ONCE,
            0
        )
    )

    override fun getRules(): Flow<List<Rule>> {
        return flowOf(rules)
    }

    override suspend fun addRule(rule: Rule) {
        rules.add(rule)
    }

    override suspend fun updateRule(rule: Rule) {
        val index = rules.indexOf(rule)
        if(index>-1){ rules[index] = rule }
    }

    override suspend fun deleteRule(id: Int) {
        rules.firstOrNull{ it.id == id }?.let { rules.remove(it) }
    }
}