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
    // consigo el contexto actual
    val context = LocalContext.current

    // obtengo el viewmodel donde guardo el estado de los permisos
    val miViewModel = viewModel<PermisosViewModel>()

    // leo los estados actuales de los permisos desde el viewmodel
    val estadoPermisos = miViewModel.permisosEstado.value

    // creo una variable para asegurarme de que solo se pidan los permisos una vez
    var yaSolicitado by remember { mutableStateOf(false) }

    // lista de permisos que necesito pedir
    val permisos = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    // launcher para pedir múltiples permisos a la vez
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultados: Map<String, Boolean> ->
        // recorro cada permiso y actualizo su estado en el viewmodel según el resultado
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

    // efecto lanzado al entrar a la pantalla para pedir permisos solo una vez
    LaunchedEffect(Unit) {
        if (!yaSolicitado) {
            yaSolicitado = true
            launcher.launch(permisos.toTypedArray())
        }
    }

    // contenedor principal de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        // organizo los elementos en columna, centrados
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // título principal de la pantalla
            Text("Estado de permisos:", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(16.dp))

            // muestro el estado de cada permiso con texto
            permisos.forEach { permiso ->
                val estado = estadoPermisos[permiso]
                val textoEstado = when (estado) {
                    null -> "Solicitando..."
                    PermisosEstado.Concedido -> "Concedido"
                    PermisosEstado.Denegado -> "Denegado"
                    PermisosEstado.DenegadoPermanentemente -> "Denegado permanentemente"
                }
                // limpio el texto del permiso quitando el prefijo
                val nombrePermiso = permiso.removePrefix("android.permisos.")
                Text("$nombrePermiso: $textoEstado")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // si todos los permisos están concedidos, navego al drawer
            if (permisos.all {
                    estadoPermisos[it] == PermisosEstado.Concedido
                }) {
                navigateToDrawer()
            }
            // si algún permiso fue denegado (pero no permanente), muestro botón para reintentar
            else if (permisos.any {
                    estadoPermisos[it] == PermisosEstado.Denegado
                }) {
                Button(onClick = {
                    launcher.launch(permisos.toTypedArray())
                }) {
                    Text("Aplicar de nuevo")
                }
            }
            // si algún permiso fue denegado permanentemente, muestro botón para ir a ajustes
            else if (permisos.any {
                    estadoPermisos[it] == PermisosEstado.DenegadoPermanentemente
                }) {
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
