package com.example.mapsapp.ui.navigation

import CreateMarkerScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.navigation.Destination.CreateMarker
import com.example.mapsapp.ui.navigation.Destination.DetalleMarker
import com.example.mapsapp.ui.navigation.Destination.Mapp
import com.example.mapsapp.ui.navigation.Destination.List
import com.example.mapsapp.ui.screens.DetalleMarkerScreen
import com.example.mapsapp.ui.screens.MapScreen
import com.example.mapsapp.ui.screens.MarkerListScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigationWrapper(navController: NavHostController, modifier: Modifier){

    NavHost(navController, startDestination = Mapp) {
        composable<Mapp> {
            MapScreen(
                onNavigateToList = { navController.navigate(List) },
                onNavigateToDetalleMarker = { navController.navigate(CreateMarker(lat = 0.0, lon = 0.0)) },
                navController = navController
            )
        }

        composable<CreateMarker> {
            val createMarker = it.toRoute<CreateMarker>()
            CreateMarkerScreen( latitud = createMarker.lat, longitud = createMarker.lon,
                navigateBack = { navController.popBackStack() })
        }

        composable<List> {
            MarkerListScreen(navigateToDetalleMarker = { markerId ->
                navController.navigate(DetalleMarker(markerId.toString()))
            })
        }

        composable<DetalleMarker> {
            val markerId = it.toRoute<DetalleMarker>()
            DetalleMarkerScreen(markerId.markerId){
                navController.navigate(List) {
                    popUpTo<List> { inclusive = true }
                }
            }
        }
    }
}







