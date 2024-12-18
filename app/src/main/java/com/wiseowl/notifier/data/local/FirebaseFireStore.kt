package com.wiseowl.notifier.data.local

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wiseowl.notifier.domain.model.User
import kotlinx.coroutines.tasks.await


object FirebaseFireStore {
    private fun User.toMap(): Map<String, String> {
        return hashMapOf<String, String>().apply {
            put(FirebaseFireStoreKey.UID.key, userId)
            put(FirebaseFireStoreKey.FIRST_NAME.key, firstName)
            put(FirebaseFireStoreKey.LAST_NAME.key, lastName)
            put(FirebaseFireStoreKey.EMAIL.key, email)
            if (profilePicture != null) {
                put(FirebaseFireStoreKey.PROFILE_PICTURE.key, profilePicture)
            }
        }
    }


    private enum class FirebaseFireStoreKey(val key: String) {
        UID("uId"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        EMAIL("email"),
        PROFILE_PICTURE("profilePicture")
    }

    suspend fun saveUser(user: User) {
        Firebase.firestore.collection("users").document(user.userId).set(user.toMap()).await()
    }

    suspend fun getUserById(id: String): User {
        val result = Firebase.firestore.collection("users").document(id).get().await()
        return User(
            userId = result.get(FirebaseFireStoreKey.UID.key).toString(),
            firstName = result.get(FirebaseFireStoreKey.FIRST_NAME.key).toString(),
            lastName = result.get(FirebaseFireStoreKey.LAST_NAME.key).toString(),
            email = result.get(FirebaseFireStoreKey.EMAIL.key).toString(),
            profilePicture = result.get(FirebaseFireStoreKey.PROFILE_PICTURE.key).toString(),
        )
    }

    suspend fun updateUser(user: User) {
        Firebase.firestore.collection("users").document(user.userId).update(user.toMap()).await()
    }
}