package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mapsapp.ui.navigation.Destination
import com.example.mapsapp.viewmodels.MyViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(onNavigateToList: () -> Unit,
              onNavigateToDetalleMarker: (String) -> Unit,
              navController: NavController,
              myViewModel: MyViewModel = viewModel()) {
    val modifier: Modifier = Modifier
    Column(modifier.fillMaxSize()) {
        val itb = LatLng(41.4534225, 2.1837151)

        val camaraPosicionEstado = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb,17f)
        }

        // Observa la lista de marcadores
        val markers by myViewModel.markersList.observeAsState(emptyList())

        // Cargar marcadores al iniciar
        LaunchedEffect(Unit) {
            myViewModel.getAllMarkers()
        }

        Column(modifier = modifier) {
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
            // Mostrar todos los marcadores
            markers.forEach { marker ->
                Marker(
                    state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                    title = marker.title,
                    snippet = marker.category,
                    onClick = {
                        onNavigateToDetalleMarker(marker.id.toString())
                        true
                    }
                )}

            }
        }
    }
}

