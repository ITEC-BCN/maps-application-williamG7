package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mapsapp.ui.navigation.Destination
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(onNavigateToList: () -> Unit, onNavigateToDetalleMarker: () -> Unit, navController: NavController) {
    val modifier: Modifier = Modifier
    Column(modifier.fillMaxSize()) {
        val itb = LatLng(41.4534225, 2.1837151)

        val camaraPosicionEstado = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb,17f)
        }
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = camaraPosicionEstado,
            onMapClick = {
                Log.d("MapScreen", "Coordenadas: $it")
            }, onMapLongClick = {
                navController.navigate(
                    Destination.CreateMarker(
                        lat = it.latitude,
                        lon = it.longitude
                    )
                )
            }
        ){
            Marker(
                state = MarkerState(position = itb),
                title = "Marker in Barcelona",
                snippet = "This is a marker in Barcelona"
            )
        }
    }
}
