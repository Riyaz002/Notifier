package com.wiseowl.notifier.data.di

import android.content.Context
import com.wiseowl.notifier.data.local.database.NotifierDatabase
import com.wiseowl.notifier.data.local.datastore.NotifierDataStore
import com.wiseowl.notifier.data.remote.FirebaseDataService
import com.wiseowl.notifier.data.remote.RemoteDataService
import com.wiseowl.notifier.data.repository.RulesRepositoryImpl
import com.wiseowl.notifier.data.repository.UserRepositoryImpl
import com.wiseowl.notifier.domain.account.Authenticator
import com.wiseowl.notifier.data.service.authentication.FirebaseAuthenticator
import com.wiseowl.notifier.data.service.location.LocationService
import com.wiseowl.notifier.data.service.location.PlacesService
import com.wiseowl.notifier.domain.repository.UserRepository

object ServiceLocator {
    private val authenticator = FirebaseAuthenticator()
    private lateinit var rulesRepository: RulesRepositoryImpl
    private lateinit var locationService: LocationService
    private lateinit var placesService: PlacesService
    private lateinit var database: NotifierDatabase
    private lateinit var userRepository: UserRepository
    private val remoteDataService: RemoteDataService = FirebaseDataService()
    private val datastore get() = NotifierDataStore
    @Volatile private var initialized = false

    fun initialize(context: Context) {
        try {
            datastore.initialize(context)
            database = NotifierDatabase.getInstance(context)
            locationService = LocationService(context)
            placesService = PlacesService(context)
            rulesRepository = RulesRepositoryImpl(database.dao, remoteDataService)
            userRepository = UserRepositoryImpl(remoteDataService)
            initialized = true
        } catch (e: Exception){}
    }

    fun getAuthenticator(): Authenticator = authenticator
    fun getUserRepository() = userRepository
    fun getRulesRepository() = rulesRepository
    fun getPlacesService() = placesService
    fun getLocationService() = locationService
}