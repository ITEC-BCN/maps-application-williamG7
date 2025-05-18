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

@OptIn(ExperimentalUuidApi::class)
@Composable
fun DetalleMarkerScreen(markerId: Int, navigateBack:() -> Unit) {
    val myViewModel: MyViewModel = viewModel()
    myViewModel.getMarker(markerId)
    val markerTitle: String by  myViewModel.markerName.observeAsState("")
    val markerUserId: UUID by myViewModel.markerUserId.observeAsState(UUID.generateUUID())
    val created_at: String by myViewModel.markerCreatedAt.observeAsState("")
    val description: String by myViewModel.markerDescription.observeAsState("")
    val longitude: Double by myViewModel.markerLongitude.observeAsState(0.0)
    val latitude: Double by myViewModel.markerLatitude.observeAsState(0.0)

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Detalles del Marcador", style = MaterialTheme.typography.headlineSmall)

        // Cada campo con etiqueta y TextField
        Column {
            Text(text = "Título:", style = MaterialTheme.typography.labelMedium)
            TextField(value = markerTitle, onValueChange = { myViewModel.editMarkerTitle(it) }, modifier = Modifier.fillMaxWidth())
        }

        Column {
            Text(text = "Usuario:", style = MaterialTheme.typography.labelMedium)
            TextField(value = markerUserId.toString(), onValueChange = { myViewModel.editMarkerUserId(UUID(it)) }, modifier = Modifier.fillMaxWidth())
        }

        Column {
            Text(text = "Fecha de creación:", style = MaterialTheme.typography.labelMedium)
            TextField(value = created_at, onValueChange = { myViewModel.editMarkerCreatedAt(it) }, modifier = Modifier.fillMaxWidth())
        }

        Column {
            Text(text = "Descripción:", style = MaterialTheme.typography.labelMedium)
            TextField(value = description, onValueChange = { myViewModel.editMarkerCategory(it) }, modifier = Modifier.fillMaxWidth())
        }

        Column {
            Text(text = "Longitud:", style = MaterialTheme.typography.labelMedium)
            TextField(value = longitude.toString(), onValueChange = { myViewModel.editMarkerLongitude(it.toDouble()) }, modifier = Modifier.fillMaxWidth())
        }

        Column {
            Text(text = "Latitud:", style = MaterialTheme.typography.labelMedium)
            TextField(value = latitude.toString(), onValueChange = { myViewModel.editMarkerLatitude(it.toDouble()) }, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            myViewModel.updateMarker(markerId, markerTitle, markerUserId, created_at, description, longitude, latitude)
            navigateBack()
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Actualizar")
        }
    }
}



