package com.example.projekatv2.service

import android.net.Uri
import com.example.projekatv2.data.Resource
import com.example.projekatv2.utils.await
import com.google.firebase.storage.FirebaseStorage

class StorageService(
    private val storage: FirebaseStorage
) {
    // Upload profile picture for a user
    suspend fun uploadProfilePicture(
        uid: String,
        image: Uri
    ): String {
        return try {
            val storageRef = storage.reference.child("profile_picture/$uid.jpg")
            val uploadTask = storageRef.putFile(image).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    // Upload main image for a sport event
    suspend fun uploadSportMainImage(
        image: Uri
    ): String {
        return try {
            val fileName = "${System.currentTimeMillis()}.jpg"
            val storageRef = storage.reference.child("sport_events_images/main_images/$fileName")
            val uploadTask = storageRef.putFile(image).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    // Upload gallery images for a sport event
    suspend fun uploadSportGalleryImages(
        images: List<Uri>
    ): List<String> {
        val downloadUrls = mutableListOf<String>()
        for (image in images) {
            try {
                val fileName = "${System.currentTimeMillis()}.jpg"
                val storageRef = storage.reference.child("sport_events_images/gallery_images/$fileName")
                val uploadTask = storageRef.putFile(image).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await()
                downloadUrls.add(downloadUrl.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return downloadUrls
    }
}
