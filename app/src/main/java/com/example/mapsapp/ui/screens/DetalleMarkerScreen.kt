package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MyViewModel
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class) // uso experimental de uuid para generar ids únicos
@Composable
fun DetalleMarkerScreen(markerId: Int, navigateBack: () -> Unit) {
    val myViewModel: MyViewModel = viewModel() // obtengo la instancia del viewmodel
    myViewModel.getMarker(markerId) // cargo los datos del marcador según su id

    // observo los valores del marcador como estados reactivos para que la UI se actualice
    val markerTitle: String by myViewModel.markerName.observeAsState("")
    val markerUserId: UUID by myViewModel.markerUserId.observeAsState(UUID.generateUUID())
    val created_at: String by myViewModel.markerCreatedAt.observeAsState("")
    val description: String by myViewModel.markerDescription.observeAsState("")
    val longitude: Double by myViewModel.markerLongitude.observeAsState(0.0)
    val latitude: Double by myViewModel.markerLatitude.observeAsState(0.0)

    Column( // columna que contiene todos los elementos verticalmente
        Modifier
            .fillMaxSize() // ocupa todo el espacio disponible
            .padding(20.dp), // margen interno de 20dp
        horizontalAlignment = Alignment.Start, // alineación horizontal a la izquierda
        verticalArrangement = Arrangement.spacedBy(12.dp) // espacio vertical entre elementos
    ) {
        Spacer(modifier = Modifier.height(24.dp)) // espacio vacío arriba
        Text(text = "Detalles del Marcador", style = MaterialTheme.typography.headlineSmall) // título principal

        // campo para el título del marcador
        Column {
            Text(text = "Título:", style = MaterialTheme.typography.labelMedium) // etiqueta texto
            TextField(
                value = markerTitle,
                onValueChange = { myViewModel.editMarkerTitle(it) }, // actualizo el título en el viewmodel
                modifier = Modifier.fillMaxWidth() // ocupa todo el ancho
            )
        }

        // campo para el id del usuario asociado al marcador
        Column {
            Text(text = "Usuario:", style = MaterialTheme.typography.labelMedium)
            TextField(
                value = markerUserId.toString(),
                onValueChange = { myViewModel.editMarkerUserId(UUID(it)) }, // actualizo user id convirtiendo texto a UUID
                modifier = Modifier.fillMaxWidth()
            )
        }

        // campo para la fecha de creación del marcador
        Column {
            Text(text = "Fecha de creación:", style = MaterialTheme.typography.labelMedium)
            TextField(
                value = created_at,
                onValueChange = { myViewModel.editMarkerCreatedAt(it) }, // actualizo fecha en el viewmodel
                modifier = Modifier.fillMaxWidth()
            )
        }

        // campo para la descripción del marcador
        Column {
            Text(text = "Descripción:", style = MaterialTheme.typography.labelMedium)
            TextField(
                value = description,
                onValueChange = { myViewModel.editMarkerCategory(it) }, // actualizo descripción en el viewmodel
                modifier = Modifier.fillMaxWidth()
            )
        }

        // campo para la longitud del marcador
        Column {
            Text(text = "Longitud:", style = MaterialTheme.typography.labelMedium)
            TextField(
                value = longitude.toString(),
                onValueChange = { myViewModel.editMarkerLongitude(it.toDouble()) }, // actualizo longitud convirtiendo texto a double
                modifier = Modifier.fillMaxWidth()
            )
        }

        // campo para la latitud del marcador
        Column {
            Text(text = "Latitud:", style = MaterialTheme.typography.labelMedium)
            TextField(
                value = latitude.toString(),
                onValueChange = { myViewModel.editMarkerLatitude(it.toDouble()) }, // actualizo latitud convirtiendo texto a double
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // espacio vacío de 24dp antes del botón

        Button(
            onClick = {
                // actualizo el marcador con todos los datos actuales
                myViewModel.updateMarker(markerId, markerTitle, markerUserId, created_at, description, longitude, latitude)
                navigateBack() // vuelvo a la pantalla anterior
            },
            modifier = Modifier.align(Alignment.CenterHorizontally) // centro el botón horizontalmente
        ) {
            Text("Actualizar") // texto del botón
        }
    }
}
