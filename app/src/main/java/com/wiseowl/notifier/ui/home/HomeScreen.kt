package com.wiseowl.notifier.ui.home

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wiseowl.notifier.ui.SharedViewModel
import com.wiseowl.notifier.ui.navigation.Home

@Preview
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
) {
    Row(modifier.background(Color.Red)) {
        Text(modifier = modifier
            .fillMaxSize()
            .clickable { navController.navigate(Home) }
            .background(Color.Red), text = "Home", style = TextStyle(color = Color.Black, fontSize = 32.sp))
    }
}