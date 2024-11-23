package com.wiseowl.notifier.authentication

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wiseowl.notifier.domain.Authenticator
import com.wiseowl.notifier.domain.util.Result

class FirebaseAuthenticator: Authenticator {
    override fun isLoggedIn(): Boolean = Firebase.auth.currentUser != null

    override fun signUp(email: String, password: String, onFinish: (Result) -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            val result = if(task.isSuccessful) Result.Success(task.result) else Result.Failure(task.exception)
            onFinish.invoke(result)
        }
    }

    override fun signIn(email: String, password: String, onFinish: (Result) -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            val result = if(task.isSuccessful) Result.Success(task.result) else Result.Failure(task.exception)
            onFinish.invoke(result)
        }
    }
}