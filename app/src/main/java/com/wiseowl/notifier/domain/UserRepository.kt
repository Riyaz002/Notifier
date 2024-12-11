package com.wiseowl.notifier.domain

import com.wiseowl.notifier.domain.model.User

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User
    suspend fun getUserById(id: String): User
    suspend fun updateUser(user: User)
}