package com.example.mapsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mapsapp.ui.navigation.Destination
import com.example.mapsapp.utils.Marker
import com.example.mapsapp.viewmodels.MyViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    onNavigateToList: () -> Unit,
    onNavigateToDetalleMarker: (Int) -> Unit,
    navController: NavController,
    myViewModel: MyViewModel = viewModel()
) {
    val modifier = Modifier.fillMaxSize()
    val itb = LatLng(41.4534225, 2.1837151)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(itb, 17f)
    }

    val markers by myViewModel.markersList.observeAsState(emptyList())
    var selectedMarker by remember { mutableStateOf<Marker?>(null) }

    // Cargar marcadores al iniciar
    LaunchedEffect(Unit) {
        myViewModel.getAllMarkers()
    }

    Column(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                Log.d("MapScreen", "Coordenadas: $it")
            },
            onMapLongClick = {
                navController.navigate(
                    Destination.CreateMarker(
                        lat = it.latitude,
                        lon = it.longitude
                    )
                )
            }
        ) {
            markers.forEach { marker ->
                Marker(
                    state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                    title = marker.title,
                    snippet = marker.description,
                    onClick = {
                        selectedMarker = marker
                        true
                    }
                )
            }
        }

        // Mostrar diálogo con detalles si hay un marcador seleccionado
        selectedMarker?.let { marker ->
            AlertDialog(
                onDismissRequest = { selectedMarker = null },
                title = { Text(marker.title) },
                text = {
                    Column {
                        Text(marker.description)
                        Spacer(modifier = Modifier.height(8.dp))

                        val imageUrl = "https://ekqeuybhtpxfbmldehua.supabase.co/storage/v1/object/public/markers/${marker.image}"

                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = "Imagen del marcador",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        // Primero navega a la lista
                        onNavigateToList()
                        // Luego navega al detalle del marcador seleccionado
                        onNavigateToDetalleMarker(marker.id)
                        selectedMarker = null
                    }) {
                        Text("Ver Detalles")
                    }
                },
                dismissButton = {
                    Button(onClick = { selectedMarker = null }) {
                        Text("Cerrar")
                    }
                }
            )
        }

    }
}
