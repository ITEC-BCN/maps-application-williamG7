package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MyViewModel
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun DetalleMarkerScreen(markerId: String, navigateBack:() -> Unit) {
    val myViewModel: MyViewModel = viewModel()
    myViewModel.getMarker(markerId)
    val markerTitle: String by  myViewModel.markerName.observeAsState("")
    val markerUserId: UUID by myViewModel.markerUserId.observeAsState(UUID.generateUUID())
    val created_at: String by myViewModel.markerCreatedAt.observeAsState("")
    val category: String by myViewModel.markerCategory.observeAsState("")
    val longitude: Double by myViewModel.markerLongitude.observeAsState(0.0)
    val latitude: Double by myViewModel.markerLatitude.observeAsState(0.0)

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        TextField(value = markerTitle, onValueChange = { myViewModel.editMarkerTitle(it) })
        TextField(value = markerUserId.toString(), onValueChange = { myViewModel.editMarkerUserId(UUID(it)) })
        TextField(value = created_at, onValueChange = { myViewModel.editMarkerCreatedAt(it) })
        TextField(value = category, onValueChange = { myViewModel.editMarkerCategory(it) })
        TextField(value = longitude.toString(), onValueChange = { myViewModel.editMarkerLongitude(it.toDouble()) })
        TextField(value = latitude.toString(), onValueChange = { myViewModel.editMarkerLatitude(it.toDouble()) })
        Button(onClick = {
            myViewModel.updateMarker(markerId, markerTitle, markerUserId, created_at, category, longitude, latitude)
            navigateBack()
        }) {
            Text("Update")
        }
    }

}

