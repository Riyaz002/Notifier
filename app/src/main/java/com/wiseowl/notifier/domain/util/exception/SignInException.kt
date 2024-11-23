package com.wiseowl.notifier.domain.util.exception

class SignInException: Exception(){
    override val message: String = "Unable to sign in${if(!cause?.message.isNullOrEmpty()) " : "+cause?.message else ""}"
}