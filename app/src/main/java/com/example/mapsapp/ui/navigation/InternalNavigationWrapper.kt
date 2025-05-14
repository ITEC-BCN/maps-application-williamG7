package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.PermisosScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InternalNavigationWrapper( ) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Destination.Permisos) {
        composable<Destination.Permisos>{
            PermisosScreen(){
                navController.navigate(Destination.Drawer)
            }
        }
        composable<Destination.Drawer> {
            DrawerScreen(
                onNavigateToMapp = { navController.navigate(Destination.Mapp) },
                onNavigateToList = { navController.navigate(Destination.List) }
            )
        }
    }

}
