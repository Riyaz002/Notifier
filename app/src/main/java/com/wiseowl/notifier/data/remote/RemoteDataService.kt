package com.wiseowl.notifier.data.remote

import com.wiseowl.notifier.domain.model.Rule
import com.wiseowl.notifier.domain.model.User

interface RemoteDataService {
    fun saveUser(user: User)
    fun updateUser(user: User)
    suspend fun getUserInfo(userId: String): User
    fun saveRule(rule: Rule)
    fun updateRule(rule: Rule)
    suspend fun getRuleById(ruleId: Int): Rule
    suspend fun getAllRules(): List<Rule>
    fun deleteRule(ruleId: Int)
}