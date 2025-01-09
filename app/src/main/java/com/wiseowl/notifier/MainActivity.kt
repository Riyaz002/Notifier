package com.wiseowl.notifier

import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.wiseowl.notifier.data.di.ServiceLocator
import com.wiseowl.notifier.ui.common.component.IndeterminateCircularIndicator
import com.wiseowl.notifier.ui.navigation.Home
import com.wiseowl.notifier.ui.navigation.Login
import com.wiseowl.notifier.ui.navigation.Root
import com.wiseowl.notifier.ui.theme.NotifierTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.wiseowl.notifier.data.service.notification.Notification
import com.wiseowl.notifier.data.service.worker.NotifierWorker
import com.wiseowl.notifier.domain.event.EventManager
import com.wiseowl.notifier.domain.exception.UnhandledEventException
import com.wiseowl.notifier.domain.event.Navigate
import com.wiseowl.notifier.domain.event.PopBackStack
import com.wiseowl.notifier.domain.event.ProgressBar
import com.wiseowl.notifier.domain.event.SnackBar
import com.wiseowl.notifier.domain.event.Vibrate
import com.wiseowl.notifier.ui.common.component.BlurBox
import com.wiseowl.notifier.ui.common.component.MovingParticle
import com.wiseowl.notifier.ui.common.component.Shape
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceLocator.initialize(this.application)

        val requestLauncher =
            registerForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { result ->
                if (!result.containsValue(false)) {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                        Notification().createNotificationChannel(this.applicationContext, "default", "notifier", NotificationManager.IMPORTANCE_HIGH)
                    }
                    NotifierWorker.schedule(applicationContext)
                }
            }
        var permissionRequired = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionRequired = arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
        }
        requestLauncher.launch(permissionRequired)

        enableEdgeToEdge()
        setContent {
            val snackBarHost = remember { SnackbarHostState() }
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            var progressBarVisibility by remember { mutableStateOf(false) }
            var padding by remember { mutableStateOf(PaddingValues()) }
            val isLoggedIn = ServiceLocator.getAuthenticator().isLoggedIn()
            val currentScreen = if (isLoggedIn) Home else Login
            EventManager.subscribe { event ->
                when (event) {
                    is SnackBar -> coroutineScope.launch {
                        snackBarHost.currentSnackbarData?.dismiss()
                        snackBarHost.showSnackbar(event.text)
                    }
                    is Navigate -> navController.navigate(event.screen)
                    is PopBackStack -> navController.popBackStack()
                    is ProgressBar -> progressBarVisibility = event.show
                    is Vibrate -> vibrate(event.type)
                    else -> throw UnhandledEventException(event)
                }
            }
            NotifierTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(padding), contentAlignment = Alignment.TopCenter
                        ) {
                            SnackbarHost(hostState = snackBarHost)
                        }
                    }
                ) { innerPadding ->
                    padding = innerPadding
                    BlurBox(Modifier.fillMaxSize().padding(padding), 80f) {
                        repeat(5){ MovingParticle(
                            size = 300.dp,
                            speed = Random.nextInt(4,12),
                            shape = Shape.CIRCLE
                        ) }
                    }
                    Root(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        currentScreen
                    )
                    if (progressBarVisibility) IndeterminateCircularIndicator()
                }
            }
        }
    }
}

fun MainActivity.vibrate(effect: Vibrate.Effect){
    if(Build.VERSION_CODES.S <= Build.VERSION.SDK_INT){
        val vibrateManager = getSystemService(VibratorManager::class.java)
        vibrateManager.vibrate(
            CombinedVibration.createParallel(VibrationEffect.createPredefined(effect.type))
        )
    }
}