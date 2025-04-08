package com.example.projekatv2.model
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
data class Sport(
    @DocumentId val id: String = "",
    val organizerId: String = "",
    val description: String = "",
    val intensity: Int = 0,
    val mainImage: String = "",
    val galleryImages: List<String> = emptyList(),
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val date: Date = Date(),
    val participants: List<String> = emptyList(),
    val sportType: String = "",
    val maxParticipants: Int = 0 // Novo polje za maksimalan broj učesnika
)

