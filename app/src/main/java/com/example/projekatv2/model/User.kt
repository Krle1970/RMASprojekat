package com.example.projekatv2.model

import com.google.firebase.firestore.DocumentId

data class SportUser(
    @DocumentId var id: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val profileImage: String = "",
    val rating: Double = 0.0 // Promenjeno iz 'points' u 'rating' sa tipom Double za ocenjivanje
)
