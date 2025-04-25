package com.example.mapsapp.utils

sealed class PermisosEstado {
    object Concedido : PermisosEstado()
    object Denegado : PermisosEstado()
    object DenegadoPermanentemente : PermisosEstado()
}
