package com.example.mapsapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mapsapp.BuildConfig
import com.example.mapsapp.utils.Marker



import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.uuid.UUID
import java.time.LocalDateTime

import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi

class MySuperBaseCliente {
    lateinit var cliente: SupabaseClient
    lateinit var storage: Storage
    private val SupabaseUrl = BuildConfig.SUPABASE_URL
    private val SupabaseKey = BuildConfig.SUPABASE_KEY

    constructor(supabaseUrl: String, supabaseKey: String) {
        cliente = createSupabaseClient(
            supabaseUrl = SupabaseUrl,
            supabaseKey = SupabaseKey
        ) {
            install(Postgrest)
            install(Storage)
        }
        storage = cliente.storage
    }

    suspend fun getAllMarkers(): List<Marker> {
        return cliente.from("Marker").select().decodeList<Marker>()
    }

    suspend fun getMarker(id: String): Marker {
        return cliente.from("Marker").select {
            filter { eq("id", id) }
        } .decodeSingle<Marker>()
    }


    @OptIn(ExperimentalUuidApi::class)
    suspend fun insertMarker(marker: Marker): PostgrestResult {
        return cliente.from("Marker").insert(marker)
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateMarker(
        id: String, title: String, user_id: UUID, created_at: String, category: String, longitude: Double, latitude: Double, imageName: String, imageFile: ByteArray
    ): PostgrestResult {
        val imageName = storage.from("images").update(path = imageName, data = imageFile)
        return cliente.from("Marker").update({
            set("title", title)
            set("user_id", user_id)
            set("created_at", created_at)
            set("category", category)
            set("longitude", longitude)
            set("latitude", latitude)
            set("image", buildImageUrl(imageFileName = imageName.path))
        }) {
            filter { eq("id", id) }
        }
    }

    suspend fun deleteMarker(id: String): PostgrestResult {
        return cliente.from("Marker").delete {
            filter { eq("id", id) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imageName = storage.from("images").upload(path = "image_${fechaHoraActual.format(formato)}.png", data = imageFile)
        return buildImageUrl(imageFileName = imageName.path)
    }

    fun buildImageUrl(imageFileName: String) = "${this.SupabaseUrl}/storage/v1/object/public/images/${imageFileName}"

    suspend fun deleteImage(imageName: String){
        val imgName = imageName.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
        cliente.storage.from("images").delete(imgName)
    }
}