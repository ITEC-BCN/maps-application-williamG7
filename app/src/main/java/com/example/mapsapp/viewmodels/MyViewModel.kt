package com.example.mapsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import com.example.mapsapp.MyApp
import com.example.mapsapp.utils.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class MyViewModel {
    val dataBase = MyApp.dataBase

    private val _markerTitle = MutableLiveData<String>()
    val markerName = _markerTitle

    private val _markerUserId = MutableLiveData<Uuid>()

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

    private val _markersList = MutableLiveData<List<Marker>>()
    val markersList = _markersList

    private var _selectedMarker: Marker? = null

    fun getAllMarkers(){
        CoroutineScope(Dispatchers.IO).launch {
            val dataBaseMarkers = dataBase.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markersList.value = dataBaseMarkers
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun insertNewMarker( title: String, user_id: Uuid, created_at: String, category: String, longitude: Double, latitude: Double){
        val newMarker = Marker(title = title, user_id = user_id, created_at = created_at, category = category, longitude = longitude, latitude = latitude)
        CoroutineScope(Dispatchers.IO).launch {
            dataBase.insertMarker(newMarker)
            getAllMarkers()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun updateMarker(id: Int, title: String, user_id: Uuid, created_at: String, category: String, longitude: Double, latitude: Double){
        CoroutineScope(Dispatchers.IO).launch {
            dataBase.updateMarker(id.toString(), title, user_id, created_at, category, longitude, latitude)
        }
    }

    fun deleteMarker(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            dataBase.deleteMarker(id.toString())
            getAllMarkers()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun getMarker(id: Int){
        if (_selectedMarker == null){
            CoroutineScope(Dispatchers.IO).launch {
                val marker = dataBase.getMarker(id.toString())
                withContext(Dispatchers.Main) {
                    _selectedMarker = marker
                    _markerTitle.value = marker.title
                    _markerUserId.value = marker.user_id
                    _markerCreatedAt.value = marker.created_at
                    _markerCategory.value = marker.category.toString()
                    _markerLongitude.value = marker.longitude
                    _markerLatitude.value = marker.latitude
                }
            }
        }
    }

    fun editMarkerTitle(title: String) {
        _markerTitle.value = title
    }
    
    @OptIn(ExperimentalUuidApi::class)
    fun editMarkerUserId(userId: Uuid) {
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
}


