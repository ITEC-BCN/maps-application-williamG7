package com.example.mapsapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import com.example.mapsapp.utils.PermisosEstado

class PermisosViewModel: ViewModel() {
    private val _permisosEstado = mutableStateOf<Map<String, PermisosEstado>>(emptyMap())
    val permisosEstado: State<Map<String, PermisosEstado>> = _permisosEstado

    fun updatePermisosEstado(permisos: String, estado: PermisosEstado){

        _permisosEstado.value = _permisosEstado.value.toMutableMap().apply {
            this[permisos] = estado
        }

    }
}
