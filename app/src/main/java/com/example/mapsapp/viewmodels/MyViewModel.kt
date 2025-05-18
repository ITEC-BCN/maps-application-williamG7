package com.example.mapsapp.viewmodels

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val _markerCategory = MutableLiveData<String>()
    val markerCategory = _markerCategory

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
        category: String,
        longitude: Double,
        latitude: Double,
        image: Bitmap? = null
    ) {
        val marker = Marker(
            title = title,
            user_id = user_id,
            created_at = created_at,
            longitude = longitude,
            category = category,
            latitude = latitude,
            image = "marker_${System.currentTimeMillis()}.png"
        )
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        CoroutineScope(Dispatchers.IO).launch {
            val imageName = dataBase.uploadImage(stream.toByteArray())
            dataBase.insertMarker(marker)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun updateMarker(id: String, title: String, user_id: UUID, created_at: String, category: String, longitude: Double, latitude: Double, image: Bitmap? = null,
        currentImageUrl: String? = null
    ) {
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 0, stream)

        val imageName = currentImageUrl?.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
            ?: "marker_${System.currentTimeMillis()}.png"
        CoroutineScope(Dispatchers.IO).launch {
            dataBase.updateMarker(
                id.toInt(),
                Marker(
                    title = title,
                    user_id = user_id,
                    created_at = created_at,
                    category = category,
                    longitude = longitude,
                    latitude = latitude,
                    image = imageName
                ),
                title,
                user_id,
                created_at,
                category,
                longitude,
                latitude,
                imageName,
                stream.toByteArray()
            )
        }
    }

    fun deleteMarker(id: Int, image: String){
        CoroutineScope(Dispatchers.IO).launch {
            dataBase.deleteMarker(id.toString())
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
                    _markerCategory.value = marker.category
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

    fun editMarkerCategory(category: String) {
        _markerCategory.value = category
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


