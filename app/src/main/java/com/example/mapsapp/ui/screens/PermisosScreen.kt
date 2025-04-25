package com.example.mapsapp.ui.screens

import androidx.compose.runtime.Composable
import android.Manifest
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
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.PermisosEstado
import com.example.mapsapp.viewmodels.PermisosViewModel

@Composable
fun PermisosScreen(navigate: Unit) {
    val activity = LocalActivity.current
    val miViewModel = viewModel<PermisosViewModel>()
    val estadoPermiso = miViewModel.permisosEstado.value
    var yaSolicitado by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        val resultado = when {
            concedido -> PermisosEstado.Concedido
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> PermisosEstado.Denegado

            else -> PermisosEstado.DenegadoPermanentemente
        }
        miViewModel.updatePermisosEstado(estadoPermiso.toString(), resultado)
    }

    LaunchedEffect(Unit) {
        if (!yaSolicitado) {
            yaSolicitado = true
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (estadoPermiso) {
            null -> {
                CircularProgressIndicator()
                Text("Solicitando permiso..")
            }

            PermisosEstado.Concedido -> Text("Permiso concedido")
            PermisosEstado.Denegado -> {
                Text("Permiso denegado")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }) {
                    Text("Solicitar nuevamente")
                }
            }

            PermisosEstado.DenegadoPermanentemente -> {
                Text("Permiso denegado permanentemente")
            }
        }
    }
}


