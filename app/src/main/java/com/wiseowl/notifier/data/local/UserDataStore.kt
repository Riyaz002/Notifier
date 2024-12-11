package com.wiseowl.notifier.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.wiseowl.notifier.User
import java.io.InputStream
import java.io.OutputStream


object UserDataStore: Serializer<User> {
    override val defaultValue: User
        get() = User.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): User {
        try {
            return User.parseFrom(input)
        } catch (e: Exception){
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        t.writeTo(output)
    }
}