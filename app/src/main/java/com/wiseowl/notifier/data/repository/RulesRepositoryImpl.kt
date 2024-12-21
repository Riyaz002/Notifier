package com.wiseowl.notifier.data.repository

import com.wiseowl.notifier.data.local.database.Dao
import com.wiseowl.notifier.data.local.database.entity.RuleEntity.Companion.toRule
import com.wiseowl.notifier.data.local.database.entity.RuleEntity.Companion.toRuleEntity
import com.wiseowl.notifier.domain.repository.RulesRepository
import com.wiseowl.notifier.domain.model.Rule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RulesRepositoryImpl(private val dao: Dao): RulesRepository {
    override fun getRules(): Flow<List<Rule>> {
        return dao.getRules().map{ it.map { it.toRule() } }
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