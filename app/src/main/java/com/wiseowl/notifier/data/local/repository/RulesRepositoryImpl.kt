package com.wiseowl.notifier.data.local.repository

import com.wiseowl.notifier.data.local.Dao
import com.wiseowl.notifier.data.local.entity.RuleEntity.Companion.toRule
import com.wiseowl.notifier.data.local.entity.RuleEntity.Companion.toRuleEntity
import com.wiseowl.notifier.domain.RulesRepository
import com.wiseowl.notifier.domain.model.Rule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RulesRepositoryImpl(private val dao: Dao): RulesRepository {
    override fun getRules(): Flow<List<Rule>> {
        return dao.getRules().map { it.map { it.toRule() } }
    }

    override suspend fun addRule(rule: Rule) {
        dao.insertRule(rule.toRuleEntity())
    }

    override suspend fun updateRule(rule: Rule) {
        dao.insertRule(rule.toRuleEntity())
    }

    override suspend fun deleteRule(id: Int) {
        dao.deleteRule(id)
    }
}