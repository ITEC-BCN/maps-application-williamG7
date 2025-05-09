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
fun DetalleMarkerScreen(markerId: String, navigateBack: (String) -> Unit) {
    val myViewModel: MyViewModel = viewModel()
    myViewModel.getMarker(markerId)
    val markerTitle: String by  myViewModel.markerName.observeAsState("")
    val markerUserId: UUID by myViewModel.markerUserId.observeAsState(UUID.generateUUID())
    val userIdText: String by  remember { mutableStateOf(markerUserId.toString()) }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        TextField(value = markerTitle, onValueChange = { myViewModel.editMarkerTitle(it) })
        TextField(value = userIdText, onValueChange = { myViewModel.editMarkerUserId(UUID(it)) })
        Button(onClick = {
            myViewModel.updateMarker(markerId, markerTitle, markerUserId)
            navigateBack()
        }) {
            Text("Update")
        }
    }

}

