package com.wiseowl.notifier.domain.repository

import com.wiseowl.notifier.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: User)
    fun getUser(): Flow<User>
    suspend fun getUserById(id: String): User
    suspend fun updateUser(user: User)
}