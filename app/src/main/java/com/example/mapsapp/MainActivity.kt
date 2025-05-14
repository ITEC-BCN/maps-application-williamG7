package com.example.mapsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.mapsapp.ui.navigation.InternalNavigationWrapper
import com.example.mapsapp.ui.theme.MapsAppTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapsAppTheme {
               InternalNavigationWrapper()
            }
        }
    }
}



