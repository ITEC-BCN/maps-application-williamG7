package com.example.mapsapp.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

import com.google.android.gms.maps.model.Marker

import io.github.jan.supabase.postgrest.result.PostgrestResult
import java.sql.Timestamp
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MySuperBaseCliente {
    lateinit var cliente: SupabaseClient

    constructor(supabaseUrl: String, supabaseKey: String) {
        cliente = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
        }
    }

    suspend fun getAllMarkers(): List<com.example.mapsapp.utils.Marker> {
        return cliente.from("Marker").select().decodeList<com.example.mapsapp.utils.Marker>()
    }

    suspend fun getMarker(id: String): PostgrestResult {
        return cliente.from("Marker").select {
            filter {
                eq("id", id)
            }.decodeSingle<Marker>()
        }
    }

    suspend fun insertMarker(marker: com.example.mapsapp.utils.Marker): PostgrestResult {
        return cliente.from("Marker").insert(marker)
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateMarker(id: String, title: String, user_id: Uuid, created_at: Timestamp, category: String, longitude: Double, latitude: Double): PostgrestResult {
        return cliente.from("Marker").update({
            set("title", title)
            set("user_id", user_id)
            set("created_at", created_at)
            set("category", category)
            set("longitude", longitude)
            set("latitude", latitude)
        }) {
            filter { eq("id", id) }
        }
    }

    suspend fun deleteMarker(id: String): PostgrestResult {
        return cliente.from("Marker").delete {
            filter { eq("id", id) }
        }
    }
}