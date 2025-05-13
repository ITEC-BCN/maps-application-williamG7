package com.example.mapsapp.ui.screens

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.Marker
import com.example.mapsapp.viewmodels.MyViewModel
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import kotlin.uuid.ExperimentalUuidApi
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.setValue


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalUuidApi::class)
@Composable
fun MarkerListScreen(navigateToDetalleMarker1: String, navigateToDetalleMarker: (String) -> Unit){

    val myViewModel: MyViewModel = viewModel()
     val markersList by myViewModel.markersList.observeAsState(emptyList<Marker>())
    myViewModel.getAllMarkers()
    val markerTitle: String by myViewModel.markerName.observeAsState("")
    val markerUserId: UUID by myViewModel.markerUserId.observeAsState(UUID.generateUUID())
    val markerCreatedAt: String by myViewModel.markerCreatedAt.observeAsState("")
    val markerCategory: String by myViewModel.markerCategory.observeAsState("")
    val markerLongitude: Double by myViewModel.markerLongitude.observeAsState(0.0)
    val markerLatitude: Double by myViewModel.markerLatitude.observeAsState(0.0)
    val markerImage: String by myViewModel.markerImage.observeAsState("")
    
    val userImage by remember { mutableStateOf<Bitmap?>(null) }

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
            TextField(value = userIdText, onValueChange = { myViewModel.editMarkerUserId(UUID(it)) })
            TextField(value = markerCreatedAt, onValueChange = { myViewModel.editMarkerCreatedAt(it) })
            TextField(value = markerCategory, onValueChange = { myViewModel.editMarkerCategory(it) })
            TextField(value = markerLongitude.toString(), onValueChange = { myViewModel.editMarkerLongitude(it.toDouble()) })
            TextField(value = markerLatitude.toString(), onValueChange = { myViewModel.editMarkerLatitude(it.toDouble()) })
            TextField(value = markerImage.toString(), onValueChange = { myViewModel.editMarkerImage(it) })
            Button(onClick = { myViewModel.insertNewMarker(markerTitle, markerUserId, markerCreatedAt, markerCategory, markerLongitude, markerLatitude,userImage) }) {
                Text("Insert")
            }
        }
        Text("Markers List",
            fontSize = 28.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)
        LazyColumn(
            Modifier.fillMaxWidth()
                .weight(0.6f)
        ){
            items(markersList){ marker ->
                val dissmissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            myViewModel.deleteMarker(marker.id.toString(), marker.image)
                            true
                        } else {
                            false
                        }
                    }
                )
                SwipeToDismissBox(state = dissmissState, backgroundContent = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }) {
                    MarkerItem(marker){ navigateToDetalleMarker(marker.id.toString()) }
                }
            }
        }
    }
}

@Composable
fun MarkerItem(marker: Marker, navigateToDetalleMarker: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .border(width = 2.dp, Color.DarkGray)
            .clickable { navigateToDetalleMarker(marker.id.toString()) }
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(marker.title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "User ID: ${marker.id}")
        }
    }
}

