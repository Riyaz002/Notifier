package com.wiseowl.notifier.domain.account

import com.wiseowl.notifier.domain.util.Result
import com.wiseowl.notifier.domain.exception.SignInException
import com.wiseowl.notifier.domain.exception.SignUpException
import kotlin.jvm.Throws

interface Authenticator {

    fun getCurrentUserId(): String?

    fun isLoggedIn(): Boolean

    @Throws(SignUpException::class)
    fun signUp(email: String, password: String, onFinish: (Result) -> Unit)

    @Throws(SignInException::class)
    fun signIn(email: String, password: String, onFinish: (Result) -> Unit)
}