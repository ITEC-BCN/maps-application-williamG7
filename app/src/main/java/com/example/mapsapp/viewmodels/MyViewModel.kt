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
    val dataBase = MyApp.dataBase // accedo a la base de datos

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
        CoroutineScope(Dispatchers.IO).launch { // hago la llamada a la base de datos en un hilo IO
            val dataBaseMarkers = dataBase.getAllMarkers() // obtengo todos los marcadores
            withContext(Dispatchers.Main) {
                _markersList.value = dataBaseMarkers // actualizo la lista observable en hilo principal
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
        CoroutineScope(Dispatchers.IO).launch { // inserto marcador
            try {
                // convierto la imagen a bytes solo si existe y no está vacía
                val imageBytes = image?.let { bitmap ->
                    ByteArrayOutputStream().use { stream ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) // convierto a PNG con calidad máxima
                        stream.toByteArray().takeIf { it.isNotEmpty() } // valido que no esté vacío
                    }
                }

                // si imageBytes no es null subo la imagen a la bd y obtengo url
                val imageUrl = imageBytes?.let { bytes ->
                    dataBase.uploadImage(bytes)
                } ?: "" // si no hay imagen dejo url vacía

                // inserto marcador con todos los datos incluyendo url imagen
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
                // capturo error y muestro mensaje por consola
                println("Error al insertar marcador: ${e.message}")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun updateMarker(id: Int, title: String, user_id: UUID, created_at: String, description: String, longitude: Double, latitude: Double, image: Bitmap? = null,
                     currentImageUrl: String? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch { // actualizo marcador
            val imageBytes = image?.let { bitmap ->
                ByteArrayOutputStream().use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) // convierto nueva imagen a PNG si hay
                    stream.toByteArray().takeIf { it.isNotEmpty() } // valido bytes no vacíos
                }
            }

            // genero nombre para la imagen, si no existe url actual genero con timestamp
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
                imageFile = imageBytes // paso bytes de imagen para subir
            )
        }
    }

    fun deleteMarker(id: Int, image: String){
        CoroutineScope(Dispatchers.IO).launch { // borro marcador y su imagen en background
            dataBase.deleteMarker(id) // borro marcador por id
            dataBase.deleteImage(image) // borro imagen asociada
            getAllMarkers() // actualizo lista tras borrar
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun getMarker(id: Int){
        if (_selectedMarker == null){ // si no hay marcador seleccionado
            CoroutineScope(Dispatchers.IO).launch { // obtengo marcador por id
                val marker = dataBase.getMarker(id)
                withContext(Dispatchers.Main) {
                    _selectedMarker = marker // guardo marcador localmente
                    // actualizo live data con los datos del marcador para mostrar en UI
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
