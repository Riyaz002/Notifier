package com.wiseowl.notifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.wiseowl.notifier.login.LOGGED_IN
import com.wiseowl.notifier.login.RESULT_CODE
import com.wiseowl.notifier.ui.SharedViewModel
import com.wiseowl.notifier.ui.navigation.Root
import com.wiseowl.notifier.ui.theme.NotifierTheme

class MainActivity : ComponentActivity() {
    private val loginState = mutableStateOf(false)
    private val launchActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_CODE){
            val loggedIn = result.data?.getBooleanExtra(LOGGED_IN, false) ?: false
            loginState.value = loggedIn
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        if(Firebase.auth.currentUser==null){
//            val loginIntent = Intent(this, LoginActivity::class.java)
//            launchActivity.launch(loginIntent)
//        }
        setContent {
            val navController = rememberNavController()
            NotifierTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Root(modifier = Modifier.padding(innerPadding), navController = navController)
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