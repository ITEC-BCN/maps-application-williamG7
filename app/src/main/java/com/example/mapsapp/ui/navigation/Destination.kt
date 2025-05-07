package com.example.mapsapp.ui.navigation

import kotlinx.serialization.Serializable

open class Destination {

    @Serializable
    object  Permisos : Destination()

    @Serializable
    object  Drawer : Destination()

    @Serializable
    object  Mapp : Destination()

    @Serializable
    object List: Destination()

    @Serializable
    object CreateMarker : Destination()

    @Serializable
    object  DetalleMarker : Destination()


}