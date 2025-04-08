package com.example.projekatv2.data

import android.net.Uri
import com.example.projekatv2.model.Sport
import com.google.android.gms.maps.model.LatLng
import java.util.Date

interface SportRepository {
    suspend fun getAllSports(): Resource<List<Sport>>
    suspend fun saveSportData(
        description: String,
        intensity: Int,
        mainImage: Uri,
        galleryImages: List<Uri>,
        location: LatLng,
        date: Date,
        numberOfPeople: Int, // Dodato polje za broj učesnika
        sportType: String     // Dodato polje za tip sporta
    ): Resource<String>
    suspend fun getSportById(sportId: String): Resource<Sport>
    suspend fun getUserSports(uid: String): Resource<List<Sport>>
}
