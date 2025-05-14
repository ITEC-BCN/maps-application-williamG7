package com.example.mapsapp.ui.screens

import androidx.compose.runtime.Composable
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.PermisosEstado
import com.example.mapsapp.viewmodels.PermisosViewModel
import android.provider.Settings
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun PermisosScreen(navigateDrawer: () -> Unit) {
    val context = LocalContext.current
    val miViewModel = viewModel<PermisosViewModel>()
    val estadoPermisos = miViewModel.permisosEstado.value
    var yaSolicitado by remember { mutableStateOf(false) }

    val permisos = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultados: Map<String, Boolean> ->
        permisos.forEach { permiso ->
            val concedido = resultados[permiso] ?: false
            val estado = when {
                concedido -> PermisosEstado.Concedido
                ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permiso) ->
                    PermisosEstado.Denegado
                else -> PermisosEstado.DenegadoPermanentemente
            }
            miViewModel.updatePermisosEstado(permiso, estado)
        }
    }

    LaunchedEffect(Unit) {
        if (!yaSolicitado) {
            yaSolicitado = true
            launcher.launch(permisos.toTypedArray())
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Estado de permisos:", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        if (estadoPermisos.isEmpty()) {
            CircularProgressIndicator()
            Text("Solicitando permisos...")
        } else {
            permisos.forEach { permiso ->
                val estado = estadoPermisos[permiso]
                val nombrePermiso = permiso.split(".").last()
                val textoEstado = when (estado) {
                    PermisosEstado.Concedido -> "Concedido"
                    PermisosEstado.Denegado -> "Denegado"
                    PermisosEstado.DenegadoPermanentemente -> "Denegado permanentemente"
                    else -> "Desconocido"
                }
                Text("$nombrePermiso: $textoEstado")
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (estadoPermisos.any { it.value == PermisosEstado.Denegado }) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    launcher.launch(permisos.toTypedArray())
                }) {
                    Text("Solicitar nuevamente")
                }
            }

            if (estadoPermisos.any { it.value == PermisosEstado.DenegadoPermanentemente }) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Ir a configuración")
                }
            }
        }
    }
}




