package com.example.mapsapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mapsapp.ui.navigation.Destination

enum class DrawerItem(
    val icon: ImageVector,
    val text: String,
    val route: Destination
) {
    MAPP(Icons.Default.LocationOn, "Mapa", Destination.Mapp),
    LIST(Icons.Default.List, "Lista", Destination.List)
}