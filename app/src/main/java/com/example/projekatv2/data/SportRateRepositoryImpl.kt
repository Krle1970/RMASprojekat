package com.example.projekatv2.data

import com.example.projekatv2.model.Sport
import com.example.projekatv2.model.SportRate
import com.example.projekatv2.service.DatabaseService
import com.example.projekatv2.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SportRateRepositoryImpl : SportRateRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val databaseService = DatabaseService(firestoreInstance)

    override suspend fun getSportRates(sportId: String): Resource<List<SportRate>> {
        return try {
            val snapshot = firestoreInstance.collection("sport_rates")
                .whereEqualTo("sportId", sportId)
                .get()
                .await()
            val rates = snapshot.toObjects(SportRate::class.java)
            Resource.Success(rates)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserRates(): Resource<List<SportRate>> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val snapshot = firestoreInstance.collection("sport_rates")
                    .whereEqualTo("userId", currentUser.uid)
                    .get()
                    .await()
                val rates = snapshot.toObjects(SportRate::class.java)
                Resource.Success(rates)
            } else {
                Resource.Failure(Exception("No current user logged in"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserAdForSport(): Resource<List<SportRate>> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val snapshot = firestoreInstance.collection("sport_rates")
                    .whereEqualTo("organizerId", currentUser.uid)
                    .get()
                    .await()
                val rates = snapshot.toObjects(SportRate::class.java)
                Resource.Success(rates)
            } else {
                Resource.Failure(Exception("No current user logged in"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addRate(
        sportId: String,
        rate: Int,
        sport: Sport
    ): Resource<String> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val sportRate = SportRate(
                    userId = currentUser.uid,
                    sportId = sportId,
                    rate = rate
                )
                databaseService.saveRateData(sportRate)
            }
            Resource.Success("Successfully added sport rate")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun updateRate(rateId: String, rate: Int): Resource<String> {
        return try {
            val documentRef = firestoreInstance.collection("sport_rates").document(rateId)
            documentRef.update("rate", rate).await()
            Resource.Success(rateId)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}
