package com.wiseowl.notifier.login

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.wiseowl.notifier.login.ui.LoginScreen
import com.wiseowl.notifier.ui.theme.NotifierTheme

const val RESULT_CODE = 11
const val LOGGED_IN = "logged_in"

class LoginActivity: ComponentActivity() {

    private lateinit var viewModel: LoginViewModel
    
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class]
        enableEdgeToEdge()
        setContent {
            NotifierTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }


}