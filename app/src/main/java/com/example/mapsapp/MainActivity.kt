package com.example.mapsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.MainNavigationWrapper
import com.example.mapsapp.ui.theme.MapsAppTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapsAppTheme {
                MainNavigationWrapper(
                    navController = rememberNavController(),
                    modifier = Modifier
                )
            }
        }
    }
}



