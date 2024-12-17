package com.wiseowl.notifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.wiseowl.notifier.data.ServiceLocator
import com.wiseowl.notifier.domain.event.ProgressBarEvent
import com.wiseowl.notifier.domain.event.SnackBarEvent
import com.wiseowl.notifier.ui.common.component.IndeterminateCircularIndicator
import com.wiseowl.notifier.ui.navigation.Home
import com.wiseowl.notifier.ui.navigation.Login
import com.wiseowl.notifier.ui.navigation.Navigator
import com.wiseowl.notifier.ui.navigation.Root
import com.wiseowl.notifier.ui.theme.NotifierTheme
import androidx.compose.ui.Alignment
import com.wiseowl.notifier.data.local.NotifierDataStore
import com.wiseowl.notifier.ui.navigation.Navigate
import com.wiseowl.notifier.ui.navigation.Pop
import com.wiseowl.notifier.ui.navigation.Registration
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotifierDataStore.initialize(this.application)
        ServiceLocator.initialize(this.application)

        enableEdgeToEdge()
        setContent {
            val snackBarHost = remember { SnackbarHostState() }
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            var progressBarVisibility by remember { mutableStateOf(false) }
            var padding by remember { mutableStateOf(PaddingValues()) }
            val isLoggedIn = ServiceLocator.getAuthenticator().isLoggedIn()
            val currentScreen = if(isLoggedIn) Home else Login

            LaunchedEffect(key1 = true) {
                SnackBarEvent.subscribe{ text ->
                    coroutineScope.launch {
                        snackBarHost.currentSnackbarData?.dismiss()
                        snackBarHost.showSnackbar(text)
                    }
                }
                Navigator.observe { navigationAction ->
                    when(navigationAction){
                        is Navigate -> navController.navigate(navigationAction.screen)
                        is Pop -> navController.popBackStack()
                    }
                }
                ProgressBarEvent.subscribe { visibility -> progressBarVisibility = visibility }
            }
            NotifierTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { Box(
                        Modifier
                            .fillMaxSize()
                            .padding(padding), contentAlignment = Alignment.TopCenter) {
                        SnackbarHost(hostState = snackBarHost) }
                    }
                ) { innerPadding ->
                    padding = innerPadding
                    Root(modifier = Modifier.padding(innerPadding), navController = navController, currentScreen)
                    if(progressBarVisibility) IndeterminateCircularIndicator()
                }
            }
        }
    }
}