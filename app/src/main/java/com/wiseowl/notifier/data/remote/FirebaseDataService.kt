package com.wiseowl.notifier.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.wiseowl.notifier.data.di.ServiceLocator
import com.wiseowl.notifier.domain.model.ActionType
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.model.RepeatType
import com.wiseowl.notifier.domain.model.Rule
import com.wiseowl.notifier.domain.model.User
import kotlinx.coroutines.tasks.await

class FirebaseDataService: RemoteDataService {
    private val firestore get() = Firebase.firestore
    private val userId get() =  ServiceLocator.getAuthenticator().getCurrentUserId()

    override fun saveUser(user: User) {
        firestore.collection(USERS_COLLECTION).document(user.userId).set(user)
    }

    /**
     * Just doing the same thing [saveUser] do, Will be removed in future.
     */
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
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(RULES_COLLECTION)
                .get().await().map { rule -> rules.add(rule.data.toRule()) }
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

        fun Map<String, Any>.toRule(): Rule{
            return Rule(
                id = getValue(Rule::id.name).toString().toInt(),
                name = getValue(Rule::name.name).toString(),
                description = getOrDefault(Rule::description.name, null)?.toString(),
                location = (getValue(Rule::location.name) as Map<String, Any>).toLocation(),
                radiusInMeter = getValue(Rule::radiusInMeter.name).toString().toDouble(),
                active = getValue(Rule::active.name).toString().toBoolean(),
                actionType = ActionType.valueOf(getValue(Rule::actionType.name).toString()),
                repeatType = RepeatType.valueOf(getValue(Rule::repeatType.name).toString()),
                delayInMinutes = getValue(Rule::delayInMinutes.name).toString().toInt(),
            )
        }

        private fun Map<String, Any>.toLocation(): Location{
            return Location(getValue(Location::latitude.name).toString().toDouble(),getValue(Location::longitude.name).toString().toDouble())
        }
    }
}