package com.example.mapsapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mapsapp.ui.navigation.Destination

enum class DrawerItem(
    val icon: ImageVector,
    val text: String,
    val route: Destination
) {
    HOME(Icons.Default.Home, "Home", Destination.Permisos),
    SETTINGS(Icons.Default.Settings, "Settings", Destination.Drawer),
    ABOUT(Icons.Default.Info, "About", Destination.Map),
}