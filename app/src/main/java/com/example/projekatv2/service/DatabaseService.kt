package com.example.projekatv2.service

import com.example.projekatv2.data.Resource
import com.example.projekatv2.model.Sport
import com.example.projekatv2.model.SportUser
import com.example.projekatv2.model.SportRate
import com.example.projekatv2.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class DatabaseService(
    private val firestore: FirebaseFirestore
) {
    suspend fun saveUserData(
        uid: String,
        user: SportUser
    ): Resource<String> {
        return try {
            firestore.collection("users").document(uid).set(user).await()
            Resource.Success("Uspešno dodati podaci o korisniku")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun addRating(
        uid: String,
        rating: Double
    ): Resource<String> {
        return try {
            val userDocRef = firestore.collection("users").document(uid)
            val userSnapshot = userDocRef.get().await()

            if (userSnapshot.exists()) {
                val user = userSnapshot.toObject(SportUser::class.java)
                if (user != null) {
                    val newRating = (user.rating + rating) / 2 // Prosečna ocena
                    userDocRef.update("rating", newRating).await()
                    Resource.Success("Uspešno dodata ocena korisniku")
                } else {
                    Resource.Failure(Exception("Korisnik ne postoji"))
                }
            } else {
                Resource.Failure(Exception("Korisnikov dokument ne postoji"))
            }
            Resource.Success("Uspešno dodati podaci o korisniku")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun getUserData(
        uid: String
    ): Resource<SportUser> {
        return try {
            val userDocRef = firestore.collection("users").document(uid)
            val userSnapshot = userDocRef.get().await()

            if (userSnapshot.exists()) {
                val user = userSnapshot.toObject(SportUser::class.java)
                if (user != null) {
                    Resource.Success(user)
                } else {
                    Resource.Failure(Exception("Korisnik ne postoji"))
                }
            } else {
                Resource.Failure(Exception("Korisnikov dokument ne postoji"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun saveSportData(
        sport: Sport
    ): Resource<String> {
        return try {
            firestore.collection("sports").add(sport).await()
            Resource.Success("Uspešno sačuvani podaci o sportskom događaju")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun saveRateData(
        rate: SportRate
    ): Resource<String> {
        return try {
            val result = firestore.collection("rates").add(rate).await()
            Resource.Success(result.id)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend fun updateRate(
        rid: String,
        rate: Int
    ): Resource<String> {
        return try {
            val documentRef = firestore.collection("rates").document(rid)
            documentRef.update("rate", rate).await()
            Resource.Success(rid)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}
