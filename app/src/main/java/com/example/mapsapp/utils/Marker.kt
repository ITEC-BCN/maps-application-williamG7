package com.example.mapsapp.utils

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
class Marker @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Int? = null,
    val title: String,
    val user_id: Uuid,
    val created_at: String,
    val category: String,
    val longitude: Double,
    val latitude: Double
)