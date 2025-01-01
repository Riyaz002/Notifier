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
    private val userRepository = UserRepositoryImpl.getInstance()
    private lateinit var rulesRepository: RulesRepositoryImpl
    private lateinit var locationService: LocationService
    private lateinit var placesService: PlacesService

    fun initialize(context: Context) {
        rulesRepository = RulesRepositoryImpl(NotifierDatabase.getInstance(context).dao)
        locationService = LocationService.getInstance(context)
        placesService = PlacesService.getInstance(context)
    }

    fun getAuthenticator(): Authenticator = authenticator
    fun getUserRepository() = userRepository
    fun getRulesRepository() = rulesRepository
    fun getPlacesService() = placesService
    fun getLocationService() = locationService
}