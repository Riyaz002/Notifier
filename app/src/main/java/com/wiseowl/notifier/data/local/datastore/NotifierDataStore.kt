package com.wiseowl.notifier.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.wiseowl.notifier.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Optional


object NotifierDataStore {

    private lateinit var applicationContext: Context

    fun initialize(context: Context){
        applicationContext = context
    }

    private val Context.protoDatastore: DataStore<com.wiseowl.notifier.User> by dataStore(
        fileName = "user.pb",
        serializer = UserDataStore
    )

    private val Context.preferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = "prefs")


    fun getUser() = applicationContext.protoDatastore.data.map {
        it.toUser()
    }

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
        applicationContext.protoDatastore.updateData {
            user.toUserProto()
        }
    }

    suspend fun updateSyncTime(){
        applicationContext.preferenceDataStore.edit { preferences ->
            preferences[longPreferencesKey(SYNC_TIME_KEY)] = System.currentTimeMillis()
        }
    }

    suspend fun getLastSyncTime(): Long {
        val preferences = applicationContext.preferenceDataStore.data.first()
        return preferences[longPreferencesKey(SYNC_TIME_KEY)]?:0
    }

    suspend fun updateUser(user: User) {
        applicationContext.protoDatastore.updateData {
            user.toUserProto()
        }
    }

    private const val SYNC_TIME_KEY = "sync_time"
}