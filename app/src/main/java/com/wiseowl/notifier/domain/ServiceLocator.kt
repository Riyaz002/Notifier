package com.wiseowl.notifier.domain

import com.wiseowl.notifier.authentication.FirebaseAuthenticator

object ServiceLocator {
    private val authenticator = FirebaseAuthenticator()

    fun getAuthenticator(): Authenticator = authenticator
}