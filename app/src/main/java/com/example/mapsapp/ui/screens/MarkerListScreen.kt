package com.example.mapsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.Marker
import com.example.mapsapp.viewmodels.MyViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerListScreen(navigateToDetalleMarker: (Int) -> Unit) {
    // obtengo el viewmodel para acceder a los datos de los marcadores
    val myViewModel: MyViewModel = viewModel()

    // observo el estado de carga y la lista de marcadores desde el viewmodel
    val showLoading: Boolean by myViewModel.loading.observeAsState(true)
    val markersList by myViewModel.markersList.observeAsState(emptyList<Marker>())

    // lanzo efecto al iniciar la pantalla para cargar los marcadores
    LaunchedEffect(Unit) {
        myViewModel.getAllMarkers()
    }

    // estructura base de la pantalla con barra superior
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de marcadores") }
            )
        }
    ) { innerPadding ->
        // lista que muestra todos los marcadores con swipe para eliminar
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(markersList) { marker ->
                // estado del swipe para detectar si se desliza hacia la izquierda
                val dissmissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        // si se desliza correctamente, borro el marcador
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            myViewModel.deleteMarker(marker.id, marker.image)
                            true
                        } else false
                    }
                )

                // caja que permite hacer swipe sobre el marcador para eliminarlo
                SwipeToDismissBox(
                    state = dissmissState,
                    modifier = Modifier.padding(vertical = 8.dp),
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Red)
                                .padding(end = 24.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }
                ) {
                    // llamo al composable que muestra la tarjeta del marcador
                    MarkerItem(marker = marker) {
                        navigateToDetalleMarker(marker.id)
                    }
                }
            }
        }
    }
}

@Composable
fun MarkerItem(marker: Marker, onClick: () -> Unit) {
    // caja que representa un marcador individual
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5)) // color gris claro
            .border(width = 1.dp, color = Color(0xFFBDBDBD)) // borde gris
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            // muestro el título del marcador en negrita
            Text(
                text = marker.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            // muestro el id del marcador
            Text(
                text = "User ID: ${marker.id}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            // muestro la descripción
            Text(
                text = "Description: ${marker.description}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            // muestro la fecha de creación
            Text(
                text = "Created At: ${marker.created_at}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}
