package com.wiseowl.notifier.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.wiseowl.notifier.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import java.util.Optional


object NotifierDataStore {

    private lateinit var applicationContext: Context

    fun initialize(context: Context){
        applicationContext = context
    }

    private val Context.datastore: DataStore<com.wiseowl.notifier.User> by dataStore(
        fileName = "user.pb",
        serializer = UserDataStore
    )

    fun getUser() = applicationContext.datastore.data.map { it.toUser() }

    private fun User.toUserProto(): com.wiseowl.notifier.User {
        val builder = com.wiseowl.notifier.User.newBuilder()
            .setUId(userId)
            .setFirstName(firstName)
            .setLastName(lastName)
            .setEmail(email)
        Optional.ofNullable(profilePicture).ifPresent(builder::setProfilePicture)
        return builder.build()
    }

    private fun com.wiseowl.notifier.User.toUser(): User {
        return User(
            userId = uId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            profilePicture = profilePicture
        )
    }

    suspend fun saveUser(user: User) {
        applicationContext.datastore.updateData {
            user.toUserProto()
        }
    }

    suspend fun updateUser(user: User) {
        applicationContext.datastore.updateData {
            user.toUserProto()
        }
    }
}