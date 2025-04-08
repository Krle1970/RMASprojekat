package com.example.projekatv2.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projekatv2.data.SportRepositoryImpl
import com.example.projekatv2.data.SportRateRepositoryImpl
import com.example.projekatv2.data.Resource
import com.example.projekatv2.model.Sport
import com.example.projekatv2.model.SportRate
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Date

class SportViewModel : ViewModel() {
    val repository = SportRepositoryImpl()
    val rateRepository = SportRateRepositoryImpl()

    private val _sportFlow = MutableStateFlow<Resource<String>?>(null)
    val sportFlow: StateFlow<Resource<String>?> = _sportFlow

    private val _newRate = MutableStateFlow<Resource<String>?>(null)
    val newRate: StateFlow<Resource<String>?> = _newRate

    private val _sports = MutableStateFlow<Resource<List<Sport>>>(Resource.Success(emptyList()))
    val sports: StateFlow<Resource<List<Sport>>> get() = _sports

    private val _rates = MutableStateFlow<Resource<List<SportRate>>>(Resource.Success(emptyList()))
    val rates: StateFlow<Resource<List<SportRate>>> get() = _rates

    private val _userSports = MutableStateFlow<Resource<List<Sport>>>(Resource.Success(emptyList()))
    val userSports: StateFlow<Resource<List<Sport>>> get() = _userSports

    init {
        getAllSports()
    }

    fun getAllSports() = viewModelScope.launch {
        _sports.value = repository.getAllSports()
    }

    fun saveSportData(
        description: String,
        intensity: Int,
        mainImage: Uri,
        galleryImages: List<Uri>,
        location: LatLng,
        date: Date,
        numberOfPeople: Int,
        sportType: String
    ) = viewModelScope.launch {
        _sportFlow.value = Resource.Loading
        val result = repository.saveSportData(
            description = description,
            intensity = intensity,
            mainImage = mainImage,
            galleryImages = galleryImages,
            location = location,
            date = date,
            numberOfPeople = numberOfPeople,
            sportType = sportType
        )
        _sportFlow.value = result

        if (result is Resource.Success) {
            // Immediately add the newly added event to the markers list
            val newSport = Sport(
                id = result.result,
                description = description,
                intensity = intensity,
                location = GeoPoint(location.latitude, location.longitude),
                date = date,
                sportType = sportType,
                maxParticipants = numberOfPeople
            )

            // Update the state for markers
            val currentSports = (_sports.value as? Resource.Success)?.result ?: emptyList()
            _sports.value = Resource.Success(currentSports + newSport)
        }
    }






    fun getSportRates(sportId: String) = viewModelScope.launch {
        _rates.value = Resource.Loading
        val result = rateRepository.getSportRates(sportId)
        _rates.value = result
    }
    fun loadSportById(sportId: String): Flow<Resource<Sport>> = flow {
        emit(Resource.Loading)
        try {
            val result = repository.getSportById(sportId)
            emit(result)
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }


    fun addRate(
        sportId: String,
        rate: Int,
        sport: Sport
    ) = viewModelScope.launch {
        _newRate.value = rateRepository.addRate(sportId, rate, sport)
    }

    fun updateRate(rateId: String, rate: Int) = viewModelScope.launch {
        _newRate.value = rateRepository.updateRate(rateId, rate)
    }

    fun getUserSports(uid: String) = viewModelScope.launch {
        _userSports.value = repository.getUserSports(uid)
    }
}

class SportViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SportViewModel::class.java)) {
            return SportViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
