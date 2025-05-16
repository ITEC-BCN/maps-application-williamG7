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
import com.example.mapsapp.utils.Marker
import kotlin.uuid.ExperimentalUuidApi
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import com.example.mapsapp.viewmodels.MyViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalUuidApi::class)
@Composable
fun MarkerListScreen(myViewModel:ViewModel,navigateToDetalleMarker: (Int) -> Unit) {

    val showLoading: Boolean by myViewModel.loading.observeAsState(true)
    val markersList by myViewModel.markersList.observeAsState(emptyList())

    myViewModel.getAllMarkers()

    Column(
        Modifier.fillMaxSize()
    ) {

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
                    MarkerItem(marker = marker){
                        marker.id?.let { navigateToDetalleMarker(it) }
                    }
                }
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
            Column{
                Text(marker.title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(text = "User ID: ${marker.id}")
                Text(text = "Created At: ${marker.created_at}")
                Text(text = "Category: ${marker.category}")
            }
        }
    }
}

