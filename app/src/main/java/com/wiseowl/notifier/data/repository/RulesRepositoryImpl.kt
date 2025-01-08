package com.wiseowl.notifier.data.repository

import com.wiseowl.notifier.data.local.database.Dao
import com.wiseowl.notifier.data.local.database.entity.RuleEntity.Companion.toRule
import com.wiseowl.notifier.data.local.database.entity.RuleEntity.Companion.toRuleEntity
import com.wiseowl.notifier.data.remote.RemoteDataService
import com.wiseowl.notifier.domain.repository.RulesRepository
import com.wiseowl.notifier.domain.model.Rule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RulesRepositoryImpl(private val dao: Dao, private val remoteDataService: RemoteDataService): RulesRepository {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun getRules(): Flow<List<Rule>> {
        scope.launch {
            val rules = remoteDataService.getAllRules()
            rules.forEach { dao.insertRule(it.toRuleEntity()) }
        }
        return dao.getRules().map{ it.map { it.toRule() } }
    }

    override suspend fun addRule(rule: Rule) {
        val ruleToSave = rule.copy(id = System.currentTimeMillis().toInt())
        remoteDataService.saveRule(ruleToSave)
        dao.insertRule(ruleToSave.toRuleEntity())
    }

    override suspend fun updateRule(rule: Rule) {
        remoteDataService.updateRule(rule)
        dao.insertRule(rule.toRuleEntity())
    }

    override suspend fun deleteRule(id: Int) {
        remoteDataService.deleteRule(id)
        dao.deleteRule(id)
    }
}