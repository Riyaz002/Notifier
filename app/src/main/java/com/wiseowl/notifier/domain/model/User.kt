package com.wiseowl.notifier.domain.model

data class User(
    val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val profilePicture: String?
)
