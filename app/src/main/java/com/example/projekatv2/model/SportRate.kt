package com.example.projekatv2.model

import com.google.firebase.firestore.DocumentId

data class SportRate (
    @DocumentId
    val id: String = "",
    val userId: String = "", // Korisnik koji ocenjuje
    val sportId: String = "", // Događaj koji je ocenjen
    val organizerId: String = "", // Organizator događaja
    var rate: Int = 0 // Ocena
)
