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

    fun getAllMarkers(){
        CoroutineScope(Dispatchers.IO).launch {
            val dataBaseMarkers = dataBase.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markersList.value = dataBaseMarkers
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun insertNewMarker(id:String, title: String, user_id: Uuid, created_at: String, category: String, longitude: Double, latitude: Double){
        val newMarker = Marker(id = id, title = title, user_id = user_id, created_at = created_at, category = category, longitude = longitude, latitude = latitude)
        CoroutineScope(Dispatchers.IO).launch {
            dataBase.insertMarker(newMarker)
            getAllMarkers()
        }
    }


}


