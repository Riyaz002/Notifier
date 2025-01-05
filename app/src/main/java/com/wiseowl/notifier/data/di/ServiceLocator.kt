package com.wiseowl.notifier.data.di

import android.content.Context
import com.wiseowl.notifier.data.local.database.NotifierDatabase
import com.wiseowl.notifier.data.repository.RulesRepositoryImpl
import com.wiseowl.notifier.data.repository.UserRepositoryImpl
import com.wiseowl.notifier.domain.account.Authenticator
import com.wiseowl.notifier.data.service.authentication.FirebaseAuthenticator
import com.wiseowl.notifier.data.service.location.LocationService
import com.wiseowl.notifier.data.service.location.PlacesService

object ServiceLocator {
    private val authenticator = FirebaseAuthenticator()
    private val userRepository = UserRepositoryImpl()
    private lateinit var rulesRepository: RulesRepositoryImpl
    private lateinit var locationService: LocationService
    private lateinit var placesService: PlacesService
    private lateinit var database: NotifierDatabase

    fun initialize(context: Context) {
        database = NotifierDatabase.getInstance(context)
        rulesRepository = RulesRepositoryImpl(database.dao)
        locationService = LocationService(context)
        placesService = PlacesService(context)
    }

    fun getAuthenticator(): Authenticator = authenticator
    fun getUserRepository() = userRepository
    fun getRulesRepository() = rulesRepository
    fun getPlacesService() = placesService
    fun getLocationService() = locationService
}