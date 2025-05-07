package com.example.mapsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination.CreateMarker
import com.example.mapsapp.ui.navigation.Destination.Drawer
import com.example.mapsapp.ui.navigation.Destination.Mapp
import com.example.mapsapp.ui.navigation.Destination.Permisos
import com.example.mapsapp.ui.screens.PermisosScreen
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.MapScreen


@Composable
fun MainNavigationWrapper(navController1: NavHostController, padding: Modifier) {

    val navController = rememberNavController()
    NavHost(navController,Permisos) {
        composable<Permisos>{
            PermisosScreen(navController.navigate(Drawer))
        }
        composable<Drawer>{
            DrawerScreen(navController.navigate(Mapp))
        }
        composable<Mapp>{
            MapScreen(navController as Modifier)
        }
        composable<List>{
            ListScreen(navController as Modifier)
        }
        composable<CreateMarker>{
            CreateMarkerScreen(navController as Modifier)
        }
        }
    }


}

    // permisos

    // drawer
