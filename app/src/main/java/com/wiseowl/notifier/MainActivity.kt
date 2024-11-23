package com.wiseowl.notifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.wiseowl.notifier.domain.ServiceLocator
import com.wiseowl.notifier.domain.event.ProgressBarEvent
import com.wiseowl.notifier.domain.event.SnackBarEvent
import com.wiseowl.notifier.ui.common.IndeterminateCircularIndicator
import com.wiseowl.notifier.ui.navigation.Home
import com.wiseowl.notifier.ui.navigation.Login
import com.wiseowl.notifier.ui.navigation.Navigator
import com.wiseowl.notifier.ui.navigation.Root
import com.wiseowl.notifier.ui.theme.NotifierTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackBarHost = remember { SnackbarHostState() }
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            var progressBarVisibility by remember { mutableStateOf(false) }

            LaunchedEffect(key1 = true) {
                SnackBarEvent.subscribe{ text ->
                    coroutineScope.launch { snackBarHost.showSnackbar(text) }
                }
                Navigator.observe { screenToNavigate ->
                    navController.navigate(screenToNavigate)
                }
                ProgressBarEvent.subscribe { visibility -> progressBarVisibility = visibility }
            }
            NotifierTheme {
                val isLoggedIn = ServiceLocator.getAuthenticator().isLoggedIn()
                val firstScreen = if(isLoggedIn) Home else Login
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackBarHost) }
                ) { innerPadding ->
                    if(progressBarVisibility) IndeterminateCircularIndicator()
                    Root(modifier = Modifier.padding(innerPadding), navController = navController, firstScreen)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier.background(Color.Blue)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotifierTheme {
        Greeting("Android")
    }
}