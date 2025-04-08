package com.example.projekatv2.data

import android.net.Uri
import com.example.projekatv2.model.Sport
import com.example.projekatv2.service.DatabaseService
import com.example.projekatv2.service.StorageService
import com.example.projekatv2.utils.await
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import java.util.Date
import com.example.projekatv2.data.SportRepository

class SportRepositoryImpl : SportRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val storageInstance = FirebaseStorage.getInstance()

    private val databaseService = DatabaseService(firestoreInstance)
    private val storageService = StorageService(storageInstance)

    override suspend fun getSportById(sportId: String): Resource<Sport> {
        return try {
            val documentSnapshot = firestoreInstance.collection("sports")
                .document(sportId)
                .get()
                .await()
            val sport = documentSnapshot.toObject(Sport::class.java)
            if (sport != null) {
                Resource.Success(sport)
            } else {
                Resource.Failure(Exception("Sport not found"))
            }
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun getAllSports(): Resource<List<Sport>> {
        return try {
            val snapshot = firestoreInstance.collection("sports").get().await()
            val sports = snapshot.toObjects(Sport::class.java)
            Resource.Success(sports)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun saveSportData(
        description: String,
        intensity: Int,
        mainImage: Uri,
        galleryImages: List<Uri>,
        location: LatLng,
        date: Date,
        numberOfPeople: Int, // Dodato polje za broj učesnika
        sportType: String     // Dodato polje za tip sporta
    ): Resource<String> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val mainImageUrl = storageService.uploadSportMainImage(mainImage)
                val galleryImagesUrls = storageService.uploadSportGalleryImages(galleryImages)
                val geoLocation = GeoPoint(
                    location.latitude,
                    location.longitude
                )
                val sport = Sport(
                    organizerId = currentUser.uid,
                    description = description,
                    intensity = intensity,
                    mainImage = mainImageUrl,
                    galleryImages = galleryImagesUrls,
                    location = geoLocation,
                    date = date,
                    maxParticipants = numberOfPeople, // Dodato polje za maksimalan broj učesnika
                    sportType = sportType // Dodato polje za tip sporta
                )
                databaseService.saveSportData(sport)
            }
            Resource.Success("Uspešno sačuvani svi podaci o sportskom događaju")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserSports(uid: String): Resource<List<Sport>> {
        return try {
            val snapshot = firestoreInstance.collection("sports")
                .whereEqualTo("organizerId", uid)
                .get()
                .await()
            val sports = snapshot.toObjects(Sport::class.java)
            Resource.Success(sports)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}
