package com.example.mapsapp.ui.navigation

import CreateMarkerScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination.CreateMarker
import com.example.mapsapp.ui.navigation.Destination.DetalleMarker
import com.example.mapsapp.ui.navigation.Destination.Drawer
import com.example.mapsapp.ui.navigation.Destination.Mapp
import com.example.mapsapp.ui.navigation.Destination.Permisos
import com.example.mapsapp.ui.navigation.Destination.List
import com.example.mapsapp.ui.screens.DetalleMarkerScreen
import com.example.mapsapp.ui.screens.PermisosScreen
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.MapScreen
import com.example.mapsapp.ui.screens.MarkerListScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigationWrapper(navController: NavHostController, modifier: Modifier) {

    val navController = rememberNavController()
    NavHost(navController,Permisos) {
        // permisos
        composable<Permisos>{
            PermisosScreen(navController.navigate(Drawer))
        }
        // drawer
        composable<Drawer>{
            DrawerScreen(
                onNavigateToMapp = { navController.navigate(Mapp) },
                onNavigateToList = { navController.navigate(List) }
            )
        }
        composable<Mapp>{
            MapScreen(
                onNavigateToList = { navController.navigate(List) },
                onNavigateToDetalleMarker = { navController.navigate(CreateMarker) }
            )
        }
        composable<List>{
            MarkerListScreen(navController.navigate(DetalleMarker))
        }

        composable<CreateMarker>{
            CreateMarkerScreen(navigateBack = { navController.popBackStack() })
        }

        composable<DetalleMarker> {
            backStackEntry ->
            val markerId = backStackEntry.arguments?.getString("markerId") ?: ""
            DetalleMarkerScreen(
                markerId = markerId,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}







