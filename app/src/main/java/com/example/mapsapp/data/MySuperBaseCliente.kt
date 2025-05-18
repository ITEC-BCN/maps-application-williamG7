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
    // credenciales desde buildconfig
    private val SupabaseUrl = BuildConfig.SUPABASE_URL
    private val SupabaseKey = BuildConfig.SUPABASE_KEY

    // constructor que configura la conexión
    constructor(supabaseUrl: String, supabaseKey: String) {
        cliente = createSupabaseClient(
            supabaseUrl = SupabaseUrl,
            supabaseKey = SupabaseKey
        ) {
            install(Postgrest)  // para operaciones con postgres
            install(Storage)     // para manejar archivos
        }
        storage = cliente.storage  // inicializa el storage
    }

    // obtiene todos los marcadores de la db
    suspend fun getAllMarkers(): List<Marker> {
        return cliente.from("Marker").select().decodeList<Marker>()
    }

    // busca un marcador por su id
    suspend fun getMarker(id: Int): Marker {
        return cliente.from("Marker").select {
            filter { eq("id", id) }  // filtro por id
        }.decodeSingle<Marker>()
    }

    // inserta un nuevo marcador
    @OptIn(ExperimentalUuidApi::class)
    suspend fun insertMarker(marker: Marker){
        cliente.from("Marker").insert(marker)  // inserción simple
    }

    // actualiza un marcador existente
    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateMarker(
        id: Int, update:Marker, imageFile: ByteArray? = null
    ){
        // manejo de la imagen (actualiza o mantiene la existente)
        val imageUrl = if (imageFile != null && imageFile.isNotEmpty()) {
            val imageName = update.image ?: "marker_${System.currentTimeMillis()}.png"
            storage.from("images").update(path = imageName, data = imageFile)
            buildImageUrl(imageName)
        } else {
            update.image ?: ""  // conserva la imagen actual
        }

        // actualiza todos los campos del marcador
        cliente.from("Marker").update({
            set("title", update.title)
            set("user_id", update.user_id)
            set("created_at", update.created_at)
            set("description", update.description)
            set("longitude", update.longitude)
            set("latitude", update.latitude)
            set("image", imageUrl)
        }) {
            filter { eq("id", id) }  // solo actualiza el marcador con este id
        }
    }

    // elimina un marcador por id
    @androidx.annotation.OptIn(UnstableApi::class)
    suspend fun deleteMarker(id: Int) {
        Log.d("Supabase", "Eliminando marcador con ID: $id")  // log para debug
        cliente.from("Marker").delete {
            filter { eq("id", id) }  // filtro por id
        }
    }

    // sube una imagen al storage y devuelve su url
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        // genera un nombre único basado en la fecha/hora
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imagePath = "image_${fechaHoraActual.format(formato)}.png"

        // sube la imagen y devuelve su url pública
        val imageName = storage.from("images").upload(path = imagePath, data = imageFile)
        return buildImageUrl(imageFileName = imageName.path)
    }

    // construye la url pública de una imagen
    fun buildImageUrl(imageFileName: String):String {
        return "${this.SupabaseUrl}/storage/v1/object/public/images/${imageFileName}"
    }

    // elimina una imagen del storage
    suspend fun deleteImage(imageName: String){
        // extrae el nombre del archivo de la url completa
        val imgName = imageName.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
        cliente.storage.from("images").delete(imgName)  // borra la imagen
    }
}