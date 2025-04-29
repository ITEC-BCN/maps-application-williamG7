package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.Marker
import com.example.mapsapp.viewmodels.MyViewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun MarkerScreen(navigateToDetailMarker: (String) -> Unit){

    val myViewModel = viewModel<MyViewModel>()
     val markersList by myViewModel.markersList.observeAsState(emptyList<Marker>())
    myViewModel.getAllMarkers()
    val markerTitle: String by myViewModel.markerName.observeAsState("")
    val markerUserId: String by myViewModel.markerUserId.observeAstate("")
    val markerCreatedAt: String by myViewModel.markerCreatedAt.observeAsState("")
    val markerCategory: String by myViewModel.markerCategory.observeAsState("")
    val markerLongitude: Double by myViewModel.markerLongitude.observeAsState(0.0)
    val markerLatitude: Double by myViewModel.markerLatitude.observeAsState(0.0)
    Column(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Create new marker", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            TextField(value = markerTitle, onValueChange = { myViewModel.editMarkerName(it) })
            TextField(value = markerUserId, onValueChange = { myViewModel.editMarkerUserId(it) })
            TextField(value = markerCreatedAt, onValueChange = { myViewModel.editMarkerCreatedAt(it) })
            TextField(value = markerCategory, onValueChange = { myViewModel.editMarkerCategory(it) })
            TextField(value = markerLongitude.toString(), onValueChange = { myViewModel.editMarkerLongitude(it.toDouble()) })
            TextField(value = markerLatitude.toString(), onValueChange = { myViewModel.editMarkerLatitude(it.toDouble()) })
            Button(onClick = { myViewModel.insertNewMarker(markerTitle, markerUserId, markerCreatedAt, markerCategory, markerLongitude, markerLatitude) }) {
                Text("Insert")
            }
        }
    }

}

