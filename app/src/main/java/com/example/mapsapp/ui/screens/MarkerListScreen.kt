package com.example.mapsapp.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.livedata.observeAsState
import com.example.mapsapp.utils.Marker
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MyViewModel



@Composable
fun MarkerListScreen(navigateToDetalleMarker: (Int) -> Unit) {

    val myViewModel: MyViewModel = viewModel()

    val showLoading: Boolean by myViewModel.loading.observeAsState(true)
    val markersList by myViewModel.markersList.observeAsState(emptyList<Marker>())

    LaunchedEffect(Unit) {
        myViewModel.getAllMarkers()
    }

    LazyColumn(Modifier.fillMaxWidth().padding(top = 150.dp)) {
        items(markersList) { marker ->
            val dissmissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.EndToStart) {
                        myViewModel.deleteMarker(marker.id, marker.image)
                        true
                    } else {
                        false
                    }
                }
            )
            SwipeToDismissBox(state = dissmissState, backgroundContent = {
                Box(
                    Modifier.fillMaxSize().background(Color.Red),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }) {
                MarkerItem(marker = marker) { navigateToDetalleMarker(marker.id) }
            }
        }
    }
}


@Composable
fun MarkerItem(marker: Marker, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .border(width = 2.dp, Color.DarkGray)
            .clickable { onClick() }
    ) {
        Row(Modifier.fillMaxWidth().fillMaxSize()) {
            Column {
                Text(marker.title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(text = "User ID: ${marker.id}")
                Text(text = "Created At: ${marker.created_at}")
                Text(text = "description: ${marker.description}")
            }
        }
    }
}



