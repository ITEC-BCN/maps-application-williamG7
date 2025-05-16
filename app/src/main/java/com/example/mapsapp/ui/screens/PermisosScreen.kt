package com.example.mapsapp.ui.screens

import androidx.compose.runtime.Composable
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun PermisosScreen(navigateToDrawer: () -> Unit) {
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
                ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permiso)
                    -> PermisosEstado.Denegado

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
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Estado de permisos:", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(16.dp))

            permisos.forEach { permiso ->
                val estado = estadoPermisos[permiso]
                val textoEstado = when (estado) {
                    null -> "Solicitando..."
                    PermisosEstado.Concedido -> "Concedido"
                    PermisosEstado.Denegado -> "Denegado"
                    PermisosEstado.DenegadoPermanentemente -> "Denegado permanentemente"
                }
                val nombrePermiso = permiso.removePrefix("android.permisos.")
                Text("$nombrePermiso: $textoEstado")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (permisos.all {
                estadoPermisos[it] == PermisosEstado.Concedido
            }) {
                navigateToDrawer()
            }
            else if (permisos.any{
                    estadoPermisos[it] == PermisosEstado.Denegado
                }){
                Button(onClick ={
                    launcher.launch(permisos.toTypedArray())
                }) {
                    Text("Aplicar de nuevo")
                }
            }
            else if (permisos.any{
                  estadoPermisos[it] == PermisosEstado.DenegadoPermanentemente
                }){
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context!!.packageName, null)
                    }
                    context!!.startActivity(intent)
                }) {
                    Text("Ir a configuración")
                }
            }
        }
    }
}







