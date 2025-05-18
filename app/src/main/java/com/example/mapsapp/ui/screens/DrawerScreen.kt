package com.example.mapsapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mapsapp.ui.navigation.MainNavigationWrapper
import com.example.mapsapp.utils.DrawerItem
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed) // estado inicial del drawer cerrado
    val scope = rememberCoroutineScope() // scope para lanzar corutinas dentro del composable
    var selectedItemIndex by remember { mutableStateOf(0) }

    // modal drawer que envuelve el contenido principal con navegación lateral
    ModalNavigationDrawer(
        gesturesEnabled = false, // deshabilito gestos para abrir el drawer, solo menú
        drawerContent = { // contenido que se mostrará dentro del drawer lateral
            ModalDrawerSheet { // hoja modal para el drawer que contiene los items
                DrawerItem.entries.forEachIndexed { index, drawerItem -> // itero sobre los items del drawer
                    NavigationDrawerItem(
                        icon = { Icon(imageVector = drawerItem.icon, contentDescription = drawerItem.text) }, // icono para el item
                        label = { Text(text = drawerItem.text) }, // texto para el item
                        selected = index == selectedItemIndex, // marco como seleccionado el item actual
                        onClick = { // acción al pulsar un item
                            selectedItemIndex = index // actualizo el índice seleccionado
                            scope.launch { drawerState.close() } // cierro el drawer con coroutine
                            navController.navigate(drawerItem.route) // navego a la ruta del item seleccionado
                        }
                    )
                }
            }
        },
        drawerState = drawerState // paso el estado actual del drawer para controlarlo
    ) {
        Scaffold( // scaffold que contiene topbar y contenido principal
            topBar = { // barra superior con título y botón menú
                TopAppBar(
                    title = { Text(" Mapps APP William") }, // título de la app
                    navigationIcon = { // icono para abrir el drawer
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu") // icono menú hamburguesa
                        }
                    }
                )
            }
        )
        { innerPadding ->
            MainNavigationWrapper(navController = navController, modifier = Modifier.padding(innerPadding))
            // contenedor principal que maneja la navegación dentro de la app y aplica el padding
        }
    }
}
