package com.wiseowl.notifier.domain

import com.wiseowl.notifier.domain.util.Result
import com.wiseowl.notifier.domain.exception.SignInException
import com.wiseowl.notifier.domain.exception.SignUpException
import kotlin.jvm.Throws

interface Authenticator {
    fun isLoggedIn(): Boolean

    @Throws(SignUpException::class)
    fun signUp(email: String, password: String, onFinish: (Result) -> Unit)

    @Throws(SignInException::class)
    fun signIn(email: String, password: String, onFinish: (Result) -> Unit)
}