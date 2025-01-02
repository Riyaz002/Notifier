package com.wiseowl.notifier.data.repository

import com.wiseowl.notifier.data.remote.FirebaseFireStore
import com.wiseowl.notifier.data.local.datastore.NotifierDataStore
import com.wiseowl.notifier.domain.repository.UserRepository
import com.wiseowl.notifier.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserRepositoryImpl(): UserRepository {
    private val local = NotifierDataStore
    private val remote = FirebaseFireStore
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private fun performDataSync(){
        scope.launch {
            val uId = local.getUser().first().userId
            if(uId.isNotEmpty()){
                val user = remote.getUserById(uId)
                local.saveUser(user)
                local.updateSyncTime()
            }
        }
    }

    override suspend fun saveUser(user: User) {
        remote.saveUser(user)
        local.saveUser(user)
    }

    override fun getUser(): Flow<User> {
        scope.launch {
            val lastSyncTime = local.getLastSyncTime()
            if((System.currentTimeMillis()-lastSyncTime)>30*60*1000){
                performDataSync()
            }
        }
        return local.getUser()
    }

    override suspend fun getUserById(id: String): User {
        return scope.async {
            val user = remote.getUserById(id)
            local.saveUser(user)
            user
        }.await()
    }
}