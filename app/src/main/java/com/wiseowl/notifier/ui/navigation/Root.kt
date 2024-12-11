package com.wiseowl.notifier.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wiseowl.notifier.ui.addrule.AddRuleScreen
import com.wiseowl.notifier.ui.home.HomeScreen
import com.wiseowl.notifier.ui.login.LoginScreen
import com.wiseowl.notifier.ui.registration.RegistrationScreen

@Composable
fun Root(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Screen,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Home> { HomeScreen(modifier.fillMaxSize()) }
        composable<Login> { LoginScreen(modifier) }
        composable<Registration> { RegistrationScreen(modifier) }
        composable<AddRule> { AddRuleScreen(modifier) }
    }
}