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
    val myViewModel: MyViewModel = viewModel()

    val showLoading: Boolean by myViewModel.loading.observeAsState(true)
    val markersList by myViewModel.markersList.observeAsState(emptyList<Marker>())

    LaunchedEffect(Unit) {
        myViewModel.getAllMarkers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de marcadores") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(markersList) { marker ->
                val dissmissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            myViewModel.deleteMarker(marker.id, marker.image)
                            true
                        } else false
                    }
                )
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
                    MarkerItem(marker = marker) { navigateToDetalleMarker(marker.id) }
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
            .background(Color(0xFFF5F5F5))
            .border(width = 1.dp, color = Color(0xFFBDBDBD))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = marker.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "User ID: ${marker.id}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Text(
                text = "Description: ${marker.description}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Text(
                text = "Created At: ${marker.created_at}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}






