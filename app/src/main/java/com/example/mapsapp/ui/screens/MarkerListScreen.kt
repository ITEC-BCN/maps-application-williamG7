package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.Marker
import com.example.mapsapp.viewmodels.MyViewModel
import kotlinx.uuid.fromString
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun MarkerScreen(navigateToDetailMarker: (String) -> Unit){

    val myViewModel = viewModel<MyViewModel>()
     val markersList by myViewModel.markersList.observeAsState(emptyList<Marker>())
    myViewModel.getAllMarkers()
    val markerTitle: String by myViewModel.markerName.observeAsState("")
    val markerUserId: Uuid by myViewModel.markerUserId.observeAsState(Uuid.fromString("00000000-0000-0000-0000-000000000000"))
    val markerCreatedAt: String by myViewModel.markerCreatedAt.observeAsState("")
    val markerCategory: String by myViewModel.markerCategory.observeAsState("")
    val markerLongitude: Double by myViewModel.markerLongitude.observeAsState(0.0)
    val markerLatitude: Double by myViewModel.markerLatitude.observeAsState(0.0)

    var userIdText by remember { mutableStateOf(markerUserId.toString()) }

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
            TextField(value = markerTitle, onValueChange = { myViewModel.editMarkerTitle(it) })
            TextField(value = userIdText, onValueChange = { myViewModel.editMarkerUserId(it) })
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




