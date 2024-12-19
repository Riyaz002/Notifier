package com.wiseowl.notifier.data

import android.content.Context
import com.wiseowl.notifier.data.local.NotifierDatabase
import com.wiseowl.notifier.data.local.repository.RulesRepositoryImpl
import com.wiseowl.notifier.data.local.repository.UserRepositoryImpl
import com.wiseowl.notifier.domain.account.Authenticator
import com.wiseowl.notifier.data.service.authentication.FirebaseAuthenticator

object ServiceLocator {
    private val authenticator = FirebaseAuthenticator()
    private val userRepository = UserRepositoryImpl.getInstance()
    private lateinit var rulesRepository: RulesRepositoryImpl

    fun initialize(context: Context) {
        rulesRepository = RulesRepositoryImpl(NotifierDatabase.getInstance(context).dao)
    }

    fun getAuthenticator(): Authenticator = authenticator
    fun getUserRepository() = userRepository
    fun getRulesRepository() = rulesRepository
}