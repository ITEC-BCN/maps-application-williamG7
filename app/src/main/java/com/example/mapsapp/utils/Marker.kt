package com.example.mapsapp.utils

import kotlinx.serialization.Serializable
import kotlinx.uuid.UUID
import kotlin.uuid.ExperimentalUuidApi

@Serializable
class Marker @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Int = 0,
    val title: String,
    val user_id: UUID,
    val created_at: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val image: String,
)

