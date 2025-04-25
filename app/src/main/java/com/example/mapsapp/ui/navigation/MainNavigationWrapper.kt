package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination.Permisos
import com.example.mapsapp.ui.screens.PermisosScreen


@Composable
fun MainNavigationWrapper(navController1: NavHostController, padding: Modifier) {

    val navController = rememberNavController()
    NavHost(navController,Permisos) {
        composable<Permisos>{
            PermisosScreen(
                navController.navigate(Destination.Drawer)
            )
        }
    }


}

    // permisos

    // drawer
