package com.example.mapsapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.mapsapp.BuildConfig
import com.example.mapsapp.utils.Marker



import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

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

    suspend fun getMarker(id: Int): Marker {
        return cliente.from("Marker").select {
            filter { eq("id", id) }
        } .decodeSingle<Marker>()
    }


    @OptIn(ExperimentalUuidApi::class)
    suspend fun insertMarker(marker: Marker){
        cliente.from("Marker").insert(marker)
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateMarker(
        id: Int, update:Marker, title: String, user_id: UUID, created_at: String, category: String, longitude: Double, latitude: Double, imageName: String, imageFile: ByteArray
    ){ val imageName = storage.from("images").update(path = imageName, data = imageFile)
        cliente.from("Marker").update({
            set("title", update.title)
            set("user_id", update.user_id)
            set("created_at", update.created_at)
            set("category", update.category)
            set("longitude", update.longitude)
            set("latitude", update.latitude)
            set("image", buildImageUrl(imageFileName = imageName.path))
        }) {
            filter { eq("id", id) }
        }
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    suspend fun deleteMarker(id: String) {
        Log.d("Supabase", "Eliminando marcador con ID: $id") // Debugging
        cliente.from("Marker").delete {
            filter { eq("id", id) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imagePath = "image_${fechaHoraActual.format(formato)}.png"
        val imageName = storage.from("images").upload(path = imagePath, data = imageFile)
        return buildImageUrl(imageFileName = imageName.path)
    }

    fun buildImageUrl(imageFileName: String):String {
        return "${this.SupabaseUrl}/storage/v1/object/public/images/${imageFileName}"
    }

    suspend fun deleteImage(imageName: String){
        val imgName = imageName.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
        cliente.storage.from("images").delete(imgName)
    }
}