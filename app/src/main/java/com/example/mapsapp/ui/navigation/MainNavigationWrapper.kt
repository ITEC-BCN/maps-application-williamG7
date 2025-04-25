package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination.Permisos
import com.example.mapsapp.ui.screens.PermisosScreen


@Composable
fun MainNavigationWrapper(){

    val navController = rememberNavController()
    NavHost(navController,Permisos) {
        Comparable<Permisos>{
            PermisosScreen(
                navController.navigate(Destination.Drawer)
            )
        }
    }


}

    // permisos

    // drawer
