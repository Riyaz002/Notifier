package com.wiseowl.notifier.data.di

import android.content.Context
import com.wiseowl.notifier.data.repository.RulesRepositoryImpl
import com.wiseowl.notifier.domain.model.Location
import com.wiseowl.notifier.domain.repository.RulesRepository
import com.wiseowl.notifier.domain.service.LocationService
import kotlinx.coroutines.CoroutineDispatcher

object ServiceLocator {
    fun initialize(context: Context){}
    private val locationService = object : LocationService{
        override suspend fun getCurrentLocation(
            context: Context,
            dispatcher: CoroutineDispatcher
        ) = Location(77.0, 28.13)
    }

    private val rulesRepository: RulesRepository = RulesRepositoryImpl()
    fun getLocationService() = locationService
    fun getRulesRepository() = rulesRepository
}