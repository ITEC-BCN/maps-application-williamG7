package com.example.mapsapp.viewmodels

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.BuildConfig
import com.example.mapsapp.MyApp
import com.example.mapsapp.utils.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.uuid.UUID
import java.io.ByteArrayOutputStream
import kotlin.uuid.ExperimentalUuidApi


class MyViewModel: ViewModel(){
    val dataBase = MyApp.dataBase

    private val _markerTitle = MutableLiveData<String>()
    val markerName = _markerTitle

    private val _markerUserId = MutableLiveData<UUID>()

    @OptIn(ExperimentalUuidApi::class)
    val markerUserId = _markerUserId

    private val _markerCreatedAt = MutableLiveData<String>()
    val markerCreatedAt = _markerCreatedAt

    private val _markerDescription = MutableLiveData<String>()
    val markerDescription = _markerDescription

    private val _markerLongitude = MutableLiveData<Double>()
    val markerLongitude = _markerLongitude

    private val _markerLatitude = MutableLiveData<Double>()
    val markerLatitude = _markerLatitude

    private val _markerImage = MutableLiveData<String>()
    val markerImage = _markerImage

    private val _markersList = MutableLiveData<List<Marker>>()
    val markersList = _markersList

    private val _loading = MutableLiveData<Boolean>()
    val loading = _loading

    private var _selectedMarker: Marker? = null

    fun getAllMarkers(){
        CoroutineScope(Dispatchers.IO).launch {
            val dataBaseMarkers = dataBase.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markersList.value = dataBaseMarkers
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalUuidApi::class)
    fun insertNewMarker(
        title: String,
        user_id: UUID,
        created_at: String,
        description: String,
        longitude: Double,
        latitude: Double,
        image: Bitmap? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Convertir Bitmap a ByteArray con validación
                val imageBytes = image?.let { bitmap ->
                    ByteArrayOutputStream().use { stream ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        stream.toByteArray().takeIf { it.isNotEmpty() }
                    }
                }

                // Subir imagen solo si hay datos
                val imageUrl = imageBytes?.let { bytes ->
                    dataBase.uploadImage(bytes)
                } ?: "" // Cadena vacía si no hay imagen

                // Insertar el marcador
                dataBase.insertMarker(
                    Marker(
                        title = title,
                        user_id = user_id,
                        created_at = created_at,
                        description = description,
                        longitude = longitude,
                        latitude = latitude,
                        image = imageUrl
                    )
                )
            } catch (e: Exception) {
                // Manejar el error adecuadamente
                println("Error al insertar marcador: ${e.message}")
                // Opcional: emitir un estado de error para mostrar en la UI
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun updateMarker(id: Int, title: String, user_id: UUID, created_at: String, description: String, longitude: Double, latitude: Double, image: Bitmap? = null,
        currentImageUrl: String? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val imageBytes = image?.let { bitmap ->
                ByteArrayOutputStream().use { stream ->
                    // Usar calidad 100 para PNG (0-100)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    stream.toByteArray().takeIf { it.isNotEmpty() }  // Validar que no esté vacío
                }
            }

            val imageName = currentImageUrl?.removePrefix("${BuildConfig.SUPABASE_URL}/storage/v1/object/public/images/")
                ?: "marker_${System.currentTimeMillis()}.png"
            dataBase.updateMarker(
                id,
                Marker(
                    title = title,
                    user_id = user_id,
                    created_at = created_at,
                    description = description,
                    longitude = longitude,
                    latitude = latitude,
                    image = imageName
                ),
                imageFile = imageBytes
            )
        }
    }

    fun deleteMarker(id: Int, image: String){
        CoroutineScope(Dispatchers.IO).launch {
            dataBase.deleteMarker(id)
            dataBase.deleteImage(image)
            getAllMarkers()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun getMarker(id: Int){
        if (_selectedMarker == null){
            CoroutineScope(Dispatchers.IO).launch {
                val marker = dataBase.getMarker(id)
                withContext(Dispatchers.Main) {
                    _selectedMarker = marker
                    _markerTitle.value = marker.title
                    _markerUserId.value = marker.user_id
                    _markerCreatedAt.value = marker.created_at
                    _markerDescription.value = marker.description
                    _markerLongitude.value = marker.longitude
                    _markerLatitude.value = marker.latitude
                    _markerImage.value = marker.image
                }
            }
        }
    }

    fun editMarkerTitle(title: String) {
        _markerTitle.value = title
    }

    @OptIn(ExperimentalUuidApi::class)
    fun editMarkerUserId(userId: UUID) {
        _markerUserId.value = userId
    }

    fun editMarkerCreatedAt(createdAt: String) {
        _markerCreatedAt.value = createdAt
    }

    fun editMarkerCategory(description: String) {
        _markerDescription.value = description
    }

    fun editMarkerLongitude(longitude: Double) {
        _markerLongitude.value = longitude
    }

    fun editMarkerLatitude(latitude: Double) {
        _markerLatitude.value = latitude
    }

    fun editMarkerImage(image: String) {
        _markerImage.value = image
    }
}


