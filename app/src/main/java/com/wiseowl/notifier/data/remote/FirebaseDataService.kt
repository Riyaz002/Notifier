package com.wiseowl.notifier.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.wiseowl.notifier.data.di.ServiceLocator
import com.wiseowl.notifier.domain.model.Rule
import com.wiseowl.notifier.domain.model.User
import kotlinx.coroutines.tasks.await

class FirebaseDataService: RemoteDataService {
    private val firestore get() = Firebase.firestore
    private val userId get() =  ServiceLocator.getAuthenticator().getCurrentUserId()

    override fun saveUser(user: User) {
        firestore.collection(USERS_COLLECTION).document(user.userId).set(user)
    }

    //TODO: just doing the same thing saveUser do, Remove if it is useless
    override fun updateUser(user: User) {
        firestore.collection(USERS_COLLECTION).document(user.userId).set(user)
    }

    override suspend fun getUserInfo(userId: String): User {
        val result = firestore.collection(USERS_COLLECTION).document(userId).get().await()
        val user = Gson().fromJson(result.data.toString(), User::class.java)
        return user
    }

    override fun saveRule(rule: Rule) {
        firestore.collection(USERS_COLLECTION).document("$userId/$RULES_COLLECTION/${rule.id}").set(rule)
    }

    override fun updateRule(rule: Rule) {
        firestore.collection(USERS_COLLECTION).document("$userId/$RULES_COLLECTION/${rule.id}").set(rule)
    }

    override suspend fun getRuleById(ruleId: Int): Rule {
        val result = firestore.collection(USERS_COLLECTION).document("$userId/$RULES_COLLECTION/${ruleId}").get().await()
        val rule = Gson().fromJson(result.data.toString(), Rule::class.java)
        return rule
    }

    override suspend fun getAllRules(): List<Rule> {
        val result = firestore.collection(USERS_COLLECTION).get().await()
        result.forEach { userDoc ->
            val userId = userDoc.id
            val rules = mutableListOf<Rule>()
            val gson = Gson()
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(RULES_COLLECTION)
                .get().await().map { rule ->
                    rules.add(gson.fromJson(rule.data.toString(), Rule::class.java))
                }
            return rules
        }
        return listOf()
    }

    override fun deleteRule(ruleId: Int) {
        firestore.collection(USERS_COLLECTION).document("$userId/$RULES_COLLECTION/${ruleId}").delete()
    }

    companion object{
        const val USERS_COLLECTION = "users"
        const val RULES_COLLECTION = "rules"
    }
}